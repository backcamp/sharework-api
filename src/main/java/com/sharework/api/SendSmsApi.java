package com.sharework.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharework.model.sms.MessagesDto;
import com.sharework.model.sms.ReqParamsSendSms;
import com.sharework.model.sms.SmsRequest;
import com.sharework.response.model.sms.SmsResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SendSmsApi {

    @Value("${SMS.API_KEY}")
    private String API_KEY;

    @Value("${SMS.SECRET_API_KEY}")
    private String SECRET_API_KEY;
    @Value("${SMS.SERVICE_ID}")
    private String SERVICE_ID;

    public SmsResponse sendSms(ReqParamsSendSms smsDto) {

        Long time = System.currentTimeMillis();
        List<MessagesDto> messages = new ArrayList<>();
        messages.add(new MessagesDto(smsDto.getReceiver(), smsDto.getContent()));
        SmsRequest smsRequest = new SmsRequest("SMS", "COMM", "82", smsDto.getSender(), "내용", messages);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonBody = null;
        try {
            jsonBody = objectMapper.writeValueAsString(smsRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", API_KEY);

        String sig = null; //암호화
        try {
            sig = makeSignature(time);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponse smsResponse = null;
        try {
            smsResponse = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + SERVICE_ID + "/messages"), body, SmsResponse.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return smsResponse;
    }

    public String makeSignature(Long time) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + SERVICE_ID + "/messages";
        String timestamp = time.toString();
        String accessKey = API_KEY;
        String secretKey = SECRET_API_KEY;


        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = null;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = new byte[0];
        try {
            rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
