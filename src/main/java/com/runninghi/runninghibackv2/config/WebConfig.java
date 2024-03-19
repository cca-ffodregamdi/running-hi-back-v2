package com.runninghi.runninghibackv2.config;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.runninghi.runninghibackv2")
public class WebConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 토큰 필터의 설정을 관리하는 빈입니다.
     *
     * @return JWT 토큰 필터의 설정 정보를 포함하는 FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterConfig() {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenFilter(jwtTokenProvider));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
