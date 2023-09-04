package com.sharework.interceptor;

import com.google.gson.Gson;
import com.sharework.manager.JwtManager;
import com.sharework.manager.TokenIdentification;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.meta.BasicMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenInterceptor implements HandlerInterceptor {

    private final JwtManager jwtManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpResponse, Object handler) throws Exception {
        // Controller 실행 전에 수행되는 메소드
        ErrorResponse error = null;
        BasicMeta meta;
        log.info("===== preHandler START =====");
        log.info("Request URL : {}", request.getRequestURI());
        String accessToken = request.getHeader("access-token");

        if (accessToken != null) {
            if (jwtManager.isValidToken(accessToken)) {
                return true;
            }
        }

        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setStatus(401);
        PrintWriter writer = httpResponse.getWriter();
        String errorMsg = "토큰 확인바랍니다.";
        meta = new BasicMeta(false,errorMsg);
        error = new ErrorResponse(meta);
        Gson gson = new Gson();
        String response = gson.toJson(error);
        writer.print(response);
        writer.flush();
        return false;
    }
}

