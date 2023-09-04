package com.sharework.service;

import com.sharework.dao.UserDao;
import com.sharework.manager.JwtManager;
import com.sharework.manager.TokenIdentification;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SignUpPayload;
import com.sharework.response.model.SignUpResponse;
import com.sharework.response.model.meta.BasicMeta;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity updateJwtToken(String accessToken, String refreshToken) {
        ResponseEntity response = null;
        ErrorResponse error = null;
        BasicMeta meta;

        if (accessToken == null || refreshToken == null) {
            String errorMsg = "토큰 확인바랍니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity(error, HttpStatus.OK);
            return response;
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
        final SignUpResponse result = new SignUpResponse(meta, signupPayload);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateUserType(String accessToken, String refreshToken, String type) {
        ResponseEntity response = null;
        ErrorResponse error = null;
        BasicMeta meta = null;

        if (accessToken == null || refreshToken == null) {
            String errorMsg = "토큰 확인바랍니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity(error, HttpStatus.OK);
            return response;
        }

        if (!type.toLowerCase(Locale.ROOT).equals("worker") && !type.toLowerCase(Locale.ROOT).equals("giver")) {
            String errorMsg = "type wrong";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity(error, HttpStatus.OK);
            return response;
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
            error = new ErrorResponse(finalMeta);
            response = new ResponseEntity(error, HttpStatus.OK);
            return response;
        }

        final SignUpResponse result = new SignUpResponse(finalMeta, signupPayload);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
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
