package com.sharework.service;

import com.sharework.common.ApplicationTypeEnum;
import com.sharework.dao.*;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.*;
import com.sharework.request.model.RegisterReview;
import com.sharework.response.model.Response;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.job.JobTagList;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.review.*;
import com.sharework.response.model.user.Giver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final TokenIdentification identification;
    private final UserDao userDao;
    private final ReviewDao reviewDao;
    private final UserReviewDao userReviewDao;
    private final ApplicationDao applicationDao;
    private final BaseReviewDao baseReviewDao;
    private final JobTagDao jobTagDao;
    private final JobDao jobDao;

    private final UserRateDao userRateDao;

    public APIGetReview giveUserReview(String accessToken) {
        long id = identification.getHeadertoken(accessToken);
       return giveUserReviewInfo(id);
    }

    public APIGetReview giveUserReviewById(Long id) {
        return giveUserReviewInfo(id);
    }

    public APIGetReview giveUserReviewInfo(Long id) {
        User user = userDao.findByIdAndDeleteYn(id, "N").orElseThrow();
        List<Review> reviewList = null;

        if (user.getUserType().equals("worker"))
            reviewList = reviewDao.findByWorkerIdAndReviewType(id, "GIVER");
        else
            reviewList = reviewDao.findByGiverIdAndReviewType(id, "WORKER");

        List<DetailReview> detailReview = new ArrayList<>();

        reviewList.forEach(review -> {
            User reviewUser = null;

            reviewUser = review.getReviewType().equals("WORKER") ? userDao.findByIdAndDeleteYn(review.getGiverId(), "N").orElseThrow() : userDao.findByIdAndDeleteYn(review.getWorkerId(), "N").orElseThrow();
            Giver giver = new Giver(reviewUser.getId(), reviewUser.getName(), reviewUser.getProfileImg());
            List<JobTagList> jobTagList = new ArrayList<>();

            List<JobTag> tags = jobTagDao.findByJobId(review.getJobId());
            for (JobTag tag : tags)
                jobTagList.add(new JobTagList(tag.getId(), tag.getContents()));

            detailReview.add(new DetailReview(review.getId(), giver, review.getComment(), review.getCreatedAt(), review.getStarRating(), jobTagList));
        });

        List<UserReview> userReviewList = userReviewDao.getByUserIdAndUserType(id, user.getUserType().toUpperCase());
        List<QuickReviewRank> quickReviewRankList = new ArrayList<>();
        userReviewList.forEach(userReview -> {
            String contents = baseReviewDao.getById(userReview.getBaseReviewId()).getContents();
            QuickReview quickReview = new QuickReview(userReview.getBaseReviewId(), contents);
            quickReviewRankList.add(new QuickReviewRank(quickReview, userReview.getCount()));
        });

        BasicMeta meta = new BasicMeta(true, "");
        return new APIGetReview(new APIGetReview.Payload(quickReviewRankList, detailReview), meta);
    }

    @Transactional
    public SuccessResponse insertReview(String accessToken, RegisterReview registerReview) {
        long userId = identification.getHeadertoken(accessToken);
        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow();

        Long opponentId = null;

        //worker -> giver
        if (user.getUserType().equals("worker")) {
            opponentId = jobDao.getById(registerReview.getJobId()).getUserId();

            reviewDao.save(Review.builder().workerId(userId)
                    .comment(registerReview.getComment())
                    .starRating(registerReview.getStartRating())
                    .giverId(opponentId).reviewType("WORKER").jobId(registerReview.getJobId())
                    .build());

        } else {
            //giver -> worker
            opponentId = applicationDao.getById(registerReview.getApplicationId()).getUserId();
            reviewDao.save(Review.builder().workerId(opponentId)
                    .comment(registerReview.getComment())
                    .starRating(registerReview.getStartRating())
                    .giverId(userId).reviewType("GIVER").jobId(registerReview.getJobId())
                    .build());
        }

        // 테이블하나 더 만들어서 해당 정보를 저장하는 테이블하나 해당 정보를 저장하여 보관하는 테이블하나.
        //user_review에 데이터들 모아서 합계 추산하는 로직

//            for (long baseReviewId : registerReview.getBaseReviewId()) {
//                Optional<UserReview> userReview= userReviewDao.findByUserIdAndBaseReviewId(user.getId(),baseReviewId);
//                if(userReview.isPresent()){
//                    userReview.get().getBaseReviewId()
//                }
//            }
        String opponentType = user.getUserType().equals("worker") ? "GIVER" : "WORKER";

        List<BaseReviewId> baseReviewList = baseReviewDao.getIdByUserType(opponentType);

        for (BaseReviewId baseReview : baseReviewList) {
            long baseReviewId = baseReview.getId();

            // 요청한 id가 요청한 baseReviewId에 존재한다면
            if (LongStream.of(registerReview.getBaseReviewId()).anyMatch(x -> x == baseReviewId)) {
                Optional<UserReview> userReview = userReviewDao.findByUserIdAndBaseReviewIdAndUserType(opponentId, baseReviewId, opponentType);
                Long setOpponentId = opponentId;
                userReview.ifPresentOrElse(selectUserReview -> {
                    selectUserReview.setCount(selectUserReview.getCount() + 1);
                    userReviewDao.save(selectUserReview);
                }, () -> {
                    userReviewDao.save(UserReview.builder().userId(setOpponentId).baseReviewId(baseReviewId).userType(opponentType).count(1).build());
                });
            }

            //baseReviewId가 없다면 userReview에 없다면 0으로 추가.
            else {
                if (!userReviewDao.existsByUserIdAndBaseReviewIdAndUserType(opponentId, baseReviewId, opponentType))
                    userReviewDao.save(UserReview.builder().userId(opponentId).baseReviewId(baseReviewId).userType(opponentType).count(0).build());
            }
        }

        //user Rate update
        Double rate = null;
        //giver -> worker
        rate = opponentType.equals("GIVER") ? reviewDao.getAvgRateGiver(opponentId) : reviewDao.getAvgRateWorker(opponentId);

        final double setRate = rate;
        Long setOpponentId = opponentId;
        userRateDao.findByUserTypeAndUserId(opponentType, opponentId).ifPresentOrElse(
                userRate -> {
                    userRate.setRate(setRate);
                    userRateDao.save(userRate);
                }, () -> {
                    userRateDao.save(UserRate.builder().userId(setOpponentId).userType(opponentType).rate(setRate).build());
                });

        return new SuccessResponse(new BasicMeta(true, "리뷰가 성공적으로 저장되었습니다."));
    }
}
