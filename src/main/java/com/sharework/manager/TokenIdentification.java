package com.sharework.manager;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TokenIdentification {
    @Autowired
    JwtManager jwtManager;

    public long getHeadertoken(String token) {

        String payloadUserId = jwtManager.getTokenUserid(token);
        return Long.parseLong(payloadUserId);
    }

    public HashMap<String, String> getTokenUserid(String accessToken, String refreshToken) {
        String userid = null;
        HashMap<String, String> map = new HashMap<>();

        userid = jwtManager.getTokenUserid(accessToken).toString();
        map.put("userid", userid);
        map.put("accessToken", "true");

        if(!jwtManager.isValidToken(refreshToken)){
            map.put("refreshToken", "false");
        }
        else
            map.put("refreshToken", "true");
        return map;
    }
}
