package com.runninghi.runninghibackv2.auth.apple;

import com.runninghi.runninghibackv2.application.dto.member.response.AppleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "apple-oauth", url = "https://appleid.apple.com")
public interface AppleClient {

    @GetMapping("/auth/keys")
    ApplePublicKeys getApplePublicKeys();

    @PostMapping("/auth/token")
    AppleTokenResponse appleAuth(
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_secret") String clientSecret);

}