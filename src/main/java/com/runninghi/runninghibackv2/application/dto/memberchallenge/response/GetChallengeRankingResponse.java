package com.runninghi.runninghibackv2.application.dto.memberchallenge.response;

public interface GetChallengeRankingResponse {
    Long getMemberChallengeId();
    String getRecord();
    String getNickname();
    String getProfileUrl();
    int getRank();
}
