package com.runninghi.runninghibackv2.config;

import com.runninghi.runninghibackv2.common.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.runninghi.runninghibackv2")
public class WebConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 토큰 처리를 위한 필터 빈을 구성하고 등록합니다.
     *
     * @return JwtTokenFilter 구성을 위한 FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterConfig() {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenFilter(jwtTokenProvider));
        registrationBean.addUrlPatterns("/api/v1/*");
        return registrationBean;
    }

    /**
     * RestTemplate을 사용자 정의 설정으로 구성하고 생성합니다.
     *
     * @param restTemplateBuilder RestTemplateBuilder를 통해 RestTemplate을 빌드합니다.
     * @return 구성된 RestTemplate 빈
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(5000)) // 연결 시간
                .setReadTimeout(Duration.ofMillis(5000)) // 읽기 시간
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();
    }
}
