package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.ChallengeRankResponse;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.entity.QMemberChallenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeStatus;
import com.runninghi.runninghibackv2.domain.repository.ChallengeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runninghi.runninghibackv2.domain.entity.QChallenge.challenge;
import static com.runninghi.runninghibackv2.domain.entity.QMemberChallenge.memberChallenge;

@Repository
@RequiredArgsConstructor
public class ChallengeQueryRepositoryImpl implements ChallengeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Challenge> findChallengesByStatusAndMember(ChallengeStatus status, Long memberNo) {

        return jpaQueryFactory.selectFrom(challenge)
                .where(challenge.status.eq(status)
                        .and(challenge.challengeNo.notIn(
                                JPAExpressions.select(memberChallenge.challenge.challengeNo)
                                        .from(memberChallenge)
                                        .where(memberChallenge.member.memberNo.eq(memberNo))
                        )))
                .fetch();
    }

    @Override
    public List<ChallengeRankResponse> findTop100Ranking(Long challengeNo) {

        return jpaQueryFactory
                .select(Projections.constructor(ChallengeRankResponse.class,
                        memberChallenge.memberChallengeId,
                        memberChallenge.record,
                        memberChallenge.member.nickname,
                        memberChallenge.member.profileImageUrl,
                        Expressions.numberTemplate(Long.class,
                                "RANK() OVER (ORDER BY {0} DESC)",
                                memberChallenge.record)
                ))
                .from(memberChallenge)
                .where(memberChallenge.challenge.challengeNo.eq(challengeNo))
                .limit(100)
                .fetch();
    }

    @Override
    public ChallengeRankResponse findMemberRanking(Long challengeNo, Long memberNo) {
        QMemberChallenge m2 = new QMemberChallenge("m2");

        return jpaQueryFactory
                .select(Projections.constructor(ChallengeRankResponse.class,
                        memberChallenge.memberChallengeId,
                        memberChallenge.record,
                        memberChallenge.member.nickname,
                        memberChallenge.member.profileImageUrl,
                        JPAExpressions
                                .select(m2.count().add(1))
                                .from(m2)
                                .where(m2.challenge.challengeNo.eq(memberChallenge.challenge.challengeNo)
                                        .and(m2.record.gt(memberChallenge.record)))
                ))
                .from(memberChallenge)
                .where(memberChallenge.challenge.challengeNo.eq(challengeNo)
                        .and(memberChallenge.member.memberNo.eq(memberNo)))
                .fetchOne();
    }

    @Override
    public List<MemberChallenge> findMemberChallengesByStatus(Long memberNo, ChallengeStatus status) {
        return jpaQueryFactory
                .selectFrom(memberChallenge)
                .where(memberChallenge.member.memberNo.eq(memberNo)
                        .and(memberChallenge.challenge.status.eq(status)))
                .fetch();

    }
}
