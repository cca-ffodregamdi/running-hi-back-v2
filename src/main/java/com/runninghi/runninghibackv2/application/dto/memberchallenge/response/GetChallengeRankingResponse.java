package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

public interface GetChallengeRankingResponse {
    Long getMemberChallengeId();
    float getRecord();
    String getNickname();
    String getProfileImageUrl();
    int getRank();
}
