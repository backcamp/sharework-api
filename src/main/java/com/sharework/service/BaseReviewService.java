package com.sharework.service;

import com.sharework.dao.BaseReviewDao;
import com.sharework.dao.UserDao;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.User;
import com.sharework.model.model.BaseReview;
import com.sharework.response.model.base_review.BaseReviewResponse;
import com.sharework.response.model.base_review.BaseReviewResponse.BaseReviewDto;
import com.sharework.response.model.base_review.BaseReviewResponse.BaseReviewPayload;
import com.sharework.response.model.meta.BasicMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseReviewService {

    private final BaseReviewDao baseReviewDao;
    private final TokenIdentification identification;
    private final UserDao userDao;

    public BaseReviewResponse getBaseReviewList(String accessToken) {
        long userId = identification.getHeadertoken(accessToken);
        User user = userDao.findByIdAndDeleteYn(userId,"N").orElseThrow();
        List<BaseReview> baseReviewList = null;
        String userType = user.getUserType().toLowerCase().equals("worker") ? "GIVER" : "WORKER";
        baseReviewList = baseReviewDao.getByUserType(userType.toUpperCase());

        List<BaseReviewDto> responseBaseReviews = new ArrayList<>();

        for (BaseReview baseReview : baseReviewList) {
            responseBaseReviews.add(new BaseReviewDto(baseReview.getId(), baseReview.getContents()));
        }

        BaseReviewPayload payload = new BaseReviewPayload(responseBaseReviews);
        BasicMeta meta = new BasicMeta(true, "");

        return new BaseReviewResponse(payload, meta);
    }
}
