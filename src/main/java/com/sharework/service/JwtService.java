package com.sharework.service;

import com.sharework.dao.UserDao;
import com.sharework.global.NotFoundException;
import com.sharework.manager.JwtManager;
import com.sharework.manager.TokenIdentification;
import com.sharework.response.model.SignUpPayload;
import com.sharework.response.model.SignUpResponse;
import com.sharework.response.model.meta.BasicMeta;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Locale;

@Service
public class JwtService {

    @Autowired
    JwtManager jwtManager;
    @Autowired
    TokenIdentification identification;
    @Autowired
    UserDao userDao;

    public SignUpResponse updateJwtToken(String accessToken, String refreshToken) {
        BasicMeta meta;

        if (accessToken == null || refreshToken == null) {
            throw new NotFoundException("토큰 확인바랍니다.");
        }

        HashMap<String, String> map = identification.getTokenUserid(accessToken, refreshToken);

        long userId = Long.parseLong(map.get("userid"));
        String userType = userDao.findByIdAndDeleteYn(userId,"N").orElseThrow().getUserType();

        String newAccessToken = reissuanceAccessToken(userId, userType);
        boolean checkRefreshToken = checkRefreshToken(refreshToken);

        String newRefreshToken = refreshToken;
        if (!checkRefreshToken) {
            newRefreshToken = reissuanceRefreshToken();
            userDao.changeJwt(refreshToken, userId);
        }

        SignUpPayload signupPayload = new SignUpPayload(newAccessToken, newRefreshToken);
        meta = new BasicMeta(true, "토큰이 재발급되었습니다.");
        return new SignUpResponse(signupPayload, meta);
    }

    public SignUpResponse updateUserType(String accessToken, String refreshToken, String type) {
        if (accessToken == null || refreshToken == null) {
            throw new NotFoundException("토큰 확인바랍니다.");
        }

        if (!type.toLowerCase(Locale.ROOT).equals("worker") && !type.toLowerCase(Locale.ROOT).equals("giver")) {
            throw new NotFoundException("type wrong");
        }

        HashMap<String, String> map = identification.getTokenUserid(accessToken, refreshToken);
        long userId = Long.parseLong(map.get("userid"));

        final BasicMeta finalMeta = new BasicMeta();
        final SignUpPayload signupPayload = new SignUpPayload();
        userDao.findByIdAndDeleteYn(userId,"N").ifPresentOrElse(user -> {
            String userType = type.toLowerCase(Locale.ROOT);

            String newAccessToken = reissuanceAccessToken(userId, userType);
            String newRefreshToken = refreshToken;
            if (!checkRefreshToken(refreshToken)) {
                newRefreshToken = reissuanceRefreshToken();
                user.setJwt(newRefreshToken);
            }
            user.setUserType(userType);
            userDao.save(user);

            signupPayload.setAccessToken(newAccessToken);
            signupPayload.setRefreshToken(newRefreshToken);
            String errorMsg = "타입이 성공적으로 변경되었습니다.";
            finalMeta.setStatus(true);
            finalMeta.setMessage(errorMsg);
        }, () -> {

            String errorMsg = "유저가 존재하지않습니다.";
            finalMeta.setStatus(false);
            finalMeta.setMessage(errorMsg);

        });

        if (signupPayload.getAccessToken().isEmpty()) {
            throw new NotFoundException(finalMeta.getMessage());
        }

        return new SignUpResponse(signupPayload, finalMeta);
    }

    private String reissuanceAccessToken(long userId, String userType) {

        //accessToken 생성!!
        JSONObject tokenParams = new JSONObject();
        tokenParams.put("userid", userId);
        tokenParams.put("usertype", userType);

        return jwtManager.createAccessToken(tokenParams, 60 * 60 * 24 * 7);// 30분
    }

    private boolean checkRefreshToken(String token) {
        if (token.equals("false")) {
            return false;
        }
        return true;
    }

    private String reissuanceRefreshToken() {
        //refreshToken 생성
        return jwtManager.createRefreshToken(60 * 60 * 24 * 21);// 일주일
    }
}
