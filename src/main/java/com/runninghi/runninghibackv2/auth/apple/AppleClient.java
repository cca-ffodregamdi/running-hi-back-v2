package com.runninghi.runninghibackv2.auth.apple;

import com.runninghi.runninghibackv2.application.dto.member.response.AppleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "apple-oauth", url = "https://appleid.apple.com")
public interface AppleClient {

    @GetMapping("/auth/oauth2/v2/keys")
    ApplePublicKeys getApplePublicKeys();

    @PostMapping("/auth/oauth2/v2/token")
    AppleTokenResponse appleAuth(
            @RequestParam("client_id") String clientId,
            @RequestParam("code") String code,
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_secret") String clientSecret);

    @PostMapping(value = "/auth/oauth2/v2/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revokeToken(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("client_id") String clientId,
            @RequestParam("token") String token,
            @RequestParam("token_type_hint") String tokenTypeHint
    );

}