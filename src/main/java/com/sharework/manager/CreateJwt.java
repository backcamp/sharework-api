package com.sharework.manager;

import com.sharework.model.User;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class CreateJwt {

    @Autowired
    JwtManager jwtManager;

    public String createAccessToken(User user) {
        JSONObject tokenParams = new JSONObject();
        tokenParams.put("userid", user.getId());
        tokenParams.put("usertype", user.getUserType());
        String accessToken = jwtManager.createAccessToken(tokenParams, 60*60*24*7);// 일주일
        return accessToken;
    }

    public String createRefreshToken() {
        String refreshToken = jwtManager.createRefreshToken(60 * 60 * 24 * 20);// 일주일
        return refreshToken;
    }
}
