package com.runninghi.runninghibackv2.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpClientConfiguration.class})
public class FeignClientConfig {
}
