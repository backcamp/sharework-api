package com.sharework.service;

import com.sharework.api.SendSmsApi;
import com.sharework.common.SendSmsStatusCodeEnum;
import com.sharework.dao.UserDao;
import com.sharework.manager.CreateJwt;
import com.sharework.manager.HashidsManager;
import com.sharework.manager.JwtManager;
import com.sharework.model.User;
import com.sharework.model.sms.ReqParamsSendSms;
import com.sharework.response.model.sms.SmsResponse;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SendSmsResponse;
import com.sharework.response.model.VerifiedPayload;
import com.sharework.response.model.VerifiedResponse;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.sms.SendSms;
import com.sharework.response.model.sms.SmsPayload;
import io.jsonwebtoken.Claims;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class SmsService {

    @Autowired
    SendSmsApi smsApi;

    @Autowired
    JwtManager jwtManager;

    @Autowired
    HashidsManager hashidsManager;

    @Autowired
    UserDao userDao;

    @Autowired
    CreateJwt createJwt;

    public ResponseEntity sendSms(Map<String, String> params) {
        String receiver = params.get("receiver");

        int randNum = 1000 + (int) (Math.random() * 8999);
        StringBuilder smsContent = new StringBuilder();
        smsContent.append("인증번호는 " + randNum + " 입니다. 쉐어워크 인증을 진행해주세요!");
        ReqParamsSendSms smsdto = new ReqParamsSendSms(receiver, smsContent.toString());

        // 네이버 클라우드 플랫폼 응답 회신
        SmsResponse smsResponse = smsApi.sendSms(smsdto);
        JSONObject json = new JSONObject();
        ResponseEntity response = null;
        BasicMeta meta;

        if (smsResponse.getStatusCode().equals("202")) {
            int receiverToInt = Integer.parseInt(receiver);
            String phoneNumberToEncode = hashidsManager.toEncode(receiverToInt);
            String randNumToEncode = hashidsManager.toEncode(randNum);

            JSONObject tokenParams = new JSONObject();
            tokenParams.put("phoneNumber", phoneNumberToEncode);
            tokenParams.put("verifiedNumber", randNumToEncode);

            String token = jwtManager.createAccessToken(tokenParams, 1900);

            SendSms smsAuth = new SendSms(token);
            SmsPayload smsPayload = new SmsPayload(smsAuth);
            meta = new BasicMeta(true, "인증번호를 발송하였습니다.");

            final SendSmsResponse result = new SendSmsResponse(smsPayload, meta);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            String errCode = setErrCode(smsResponse.getStatusCode());
            meta = new BasicMeta(false, errCode);
            final ErrorResponse error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
        }
        return response;
    }

    public ResponseEntity verifiedNumber(Map<String, String> params) {
        String token = params.get("token");
        ResponseEntity response = null;
        BasicMeta meta;
        try {
            Claims payload = jwtManager.getPayload(token);
            String payloadVerifiedNumber = payload.get("verifiedNumber").toString(); // Token 내 payload
            // 중verified_number추출
            long[] verifiedNumberToEncode = hashidsManager.toDecodeArray(payloadVerifiedNumber); // Array 형태로 해독결과를 얻어냄
            String plainTextVerifiedNumber = Long.toString(verifiedNumberToEncode[0]); // long -> string 변환
            String reqVerifiedNumber = params.get("verifiedNumber"); // 사용자가 입력한 인증번호
            boolean isSameVerifiedNumber = plainTextVerifiedNumber.equals(reqVerifiedNumber);

            String payloadPhoneNumber = payload.get("phoneNumber").toString();
            long[] phoneNumberToEncode = hashidsManager.toDecodeArray(payloadPhoneNumber);
            String plainTextPhoneNumber = Long.toString(phoneNumberToEncode[0]);
            plainTextPhoneNumber = '0' + plainTextPhoneNumber;
            System.out.println(plainTextPhoneNumber);
            System.out.println(isSameVerifiedNumber);

            if (isSameVerifiedNumber) {
                String userType = "", accessToken = "", refreshToken = "";
                Optional<User> user = userDao.findUserByPhoneNumberAndDeleteYn(plainTextPhoneNumber, "N");
                VerifiedPayload verifiedPayload = null;

                // user가 존재한다면
                if (user.isPresent()) {
                    accessToken = createJwt.createAccessToken(user.get());
                    refreshToken = createJwt.createRefreshToken();
                    userType = user.get().getUserType();
                    verifiedPayload = new VerifiedPayload(accessToken, refreshToken, userType);
                }
                meta = new BasicMeta(true, "");
                final VerifiedResponse result = new VerifiedResponse(verifiedPayload, meta);
                response = new ResponseEntity<>(result, HttpStatus.OK);
                return response;
            } else {
                meta = new BasicMeta(false, "인증번호가 틀립니다.");
                final ErrorResponse error = new ErrorResponse(meta);
                response = new ResponseEntity<>(error, HttpStatus.OK);
            }
        } catch (Exception e) {
            meta = new BasicMeta(false, e.getMessage());
            final ErrorResponse error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
        }
        return response;
    }

    private String setErrCode(String code) {
        String errCode = null;
        switch (code) {
            case "400":
                errCode = SendSmsStatusCodeEnum.Bad_Request.name();
                break;
            case "401":
                errCode = SendSmsStatusCodeEnum.Unauthorized.name();
                break;
            case "403":
                errCode = SendSmsStatusCodeEnum.Forbidden.name();
                break;
            case "404":
                errCode = SendSmsStatusCodeEnum.Not_Found.name();
                break;
            case "429":
                errCode = SendSmsStatusCodeEnum.Too_Many_Requests.name();
                break;
            case "500":
                errCode = SendSmsStatusCodeEnum.Internal_Server_Error.name();
                break;
        }
        return errCode;
    }
}
