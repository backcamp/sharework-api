package com.sharework.config;

import com.sharework.interceptor.AccessTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class tokenConfig implements WebMvcConfigurer {

    private final AccessTokenInterceptor accessTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<String>();
        patterns.add("/api/v3/application");
        patterns.add("/api/v3/application/hired");
        patterns.add("/api/v3/application/hired-approved/*");
        patterns.add("/api/v3/application/hired-request/*");
        patterns.add("/api/v3/userCheckList");
        patterns.add("/api/v3/userCheckList/*");
        patterns.add("/api/v3/userCheckList/get");
        patterns.add("/api/v3/job");
        patterns.add("/api/v3/job/info/{id}");
        patterns.add("/api/v3/job/previous");
        patterns.add("/api/v3/job/status");
        patterns.add("/api/v3/job/applied/*");
        patterns.add("/api/v3/job/hired/*");
        patterns.add("/api/v3/locationFavorite");
        patterns.add("/api/v3/job/proceeding");
        patterns.add("/api/v3/review");
        patterns.add("/api/v3/base-review");
        patterns.add("/api/v3/application/summary");
        patterns.add("/api/v3/user/**");

        registry.addInterceptor(accessTokenInterceptor).addPathPatterns(patterns).excludePathPatterns("/api/v3/job/cluster");

        /*
        // 가로채는 경로 설정 가능
        registry.addInterceptor(new SampleInterceptor())
                .addPathPatterns("/*") // 모든 Path에 대해서 가로챌것이다.
                // .addPathPatterns("/sample") // /sample경로에 대해서만 가로챌것이다.
                .excludePathPatterns("/sample"); // /sample 경로에 대해서는 Interceptor 가로채지 않을것이다.
        */
    }
}

