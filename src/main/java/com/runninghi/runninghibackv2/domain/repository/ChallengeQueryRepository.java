package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Challenge;

import java.util.List;

public interface ChallengeQueryRepository {
    List<Challenge> findChallengesByStatusAndMember(boolean status, Long memberNo);
}
