package com.sharework.service;

import com.sharework.dao.BaseReviewDao;
import com.sharework.dao.UserDao;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.User;
import com.sharework.model.model.BaseReview;
import com.sharework.response.model.Response;
import com.sharework.response.model.base_review.APIBaseReview;
import com.sharework.response.model.meta.BasicMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseReviewService {

    private final BaseReviewDao baseReviewDao;
    private final TokenIdentification identification;
    private final UserDao userDao;

    public ResponseEntity getBaseReviewList(String accessToken) {

        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);
        User user = userDao.findByIdAndDeleteYn(userId,"N").orElseThrow();
        List<BaseReview> baseReviewList = null;
        String userType = user.getUserType().toLowerCase().equals("worker") ? "GIVER" : "WORKER";
        baseReviewList = baseReviewDao.getByUserType(userType.toUpperCase());

        List<APIBaseReview.BaseReview> responseBaseReviews = new ArrayList<>();

        for (BaseReview baseReview : baseReviewList) {
            responseBaseReviews.add(new APIBaseReview.BaseReview(baseReview.getId(), baseReview.getContents()));
        }

        APIBaseReview.Payload payload = new APIBaseReview.Payload(responseBaseReviews);
        BasicMeta meta = new BasicMeta(true, "");

        response = new ResponseEntity<>(new APIBaseReview(payload, meta), HttpStatus.OK);
        return response;
    }
}
