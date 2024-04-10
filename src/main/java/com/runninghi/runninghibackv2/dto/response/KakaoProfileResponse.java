package com.runninghi.runninghibackv2.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfileResponse(
        @JsonProperty("id") Long kakaoId,
        @JsonProperty("properties") KakaoProfile kakaoProfile
){
    public Long getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return kakaoProfile.nickname();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record KakaoProfile(String nickname) {}
}

