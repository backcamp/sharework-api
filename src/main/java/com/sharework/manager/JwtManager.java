package com.sharework.manager;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtManager {
    private final Logger logger = LoggerFactory.getLogger(JwtManager.class);
    @Value("${security.jwt-keybase}")
    private String KEY_BASE;

    public String createAccessToken(Map<String, String> params, int expireTimeSeconds) {
        int expireTimeMsec = expireTimeSeconds * 1000;
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, expireTimeMsec);
        Date expiryDate = calendar.getTime();

        SecretKey secretKey = Keys.hmacShaKeyFor(KEY_BASE.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setClaims(params).setIssuedAt(now).setExpiration(expiryDate).signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(int expireTimeSeconds) {
        int expireTimeMsec = expireTimeSeconds * 1000;

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, expireTimeMsec);
        Date expiryDate = calendar.getTime();
        SecretKey secretKey = Keys.hmacShaKeyFor(KEY_BASE.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setIssuedAt(now).setExpiration(expiryDate).signWith(secretKey).compact();
    }

    public Claims getPayload(String authToken) {
        Claims payloadBody = null;
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(KEY_BASE.getBytes(StandardCharsets.UTF_8));
            payloadBody = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken).getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        } catch (JwtException e) {
            e.printStackTrace();
            logger.info("유효하지 않은 토큰입니다");
        }
        return payloadBody;
    }

    public boolean isValidToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(KEY_BASE.getBytes(StandardCharsets.UTF_8));
            Claims accessClaims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
            return false;
        } catch (ExpiredJwtException exception) {
            logger.info("만료된 JWT 토큰입니다.");
            System.out.println("Token Expired UserID : " + exception.getClaims().get("userid"));
            return false;
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
            return false;
        } catch (JwtException exception) {
            logger.info("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            logger.info("Token is null");
            return false;
        }
    }

    public String getTokenUserid(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(KEY_BASE.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userid").toString();
        } catch (ExpiredJwtException exception) {
            logger.info("만료된 JWT 토큰입니다.");
            System.out.println("Token Expired UserID : " + exception.getClaims().get("userid"));
            return exception.getClaims().get("userid").toString();
        }
    }
}