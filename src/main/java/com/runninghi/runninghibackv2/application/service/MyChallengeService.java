package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.*;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.request.CreateMyChallengeRequest;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.repository.ChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateMyChallengeResponse createMyChallenge(CreateMyChallengeRequest request, Long memberNo) {

        Challenge challenge = challengeRepository.findById(request.challengeNo())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByMemberNo(memberNo);

        if(memberChallengeRepository.findByMemberAndChallenge(member, challenge).isPresent()) {
            throw new IllegalArgumentException("이미 참여중인 챌린지입니다.");
        }

        MemberChallenge memberChallenge = MemberChallenge.builder()
                .challenge(challenge)
                .member(member)
                .build();

        memberChallengeRepository.save(memberChallenge);

        return CreateMyChallengeResponse.from(memberChallenge);
    }

    @Transactional(readOnly = true)
    public GetAllMyChallengeResponse getAllMyChallengesByStatus(Long memberNo, boolean status) {

        Member member = memberRepository.findByMemberNo(memberNo);
        List<MyChallengeListResponse> myChallengeList =
                memberChallengeRepository.findByMemberAndChallengeStatus(member, status).stream()
                .map(MyChallengeListResponse::from)
                .toList();
        int myChallengeCount = myChallengeList.size();

        return new GetAllMyChallengeResponse(myChallengeList, myChallengeCount);
    }

    @Transactional(readOnly = true)
    public GetMyChallengeResponse getMyChallengeByChallengeId(Long memberNo, Long challengeNo) {

        Member member = memberRepository.findByMemberNo(memberNo);
        Challenge challenge = challengeRepository.findById(challengeNo)
                .orElseThrow(EntityNotFoundException::new);

        MemberChallenge myChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseThrow(EntityNotFoundException::new);

        List<GetChallengeRankingResponse> challengeRanking = memberChallengeRepository.findChallengeRanking(challengeNo);
        GetChallengeRankingResponse memberRanking = memberChallengeRepository.findMemberRanking(challengeNo, memberNo);

        return GetMyChallengeResponse.from(myChallenge, challengeRanking, memberRanking);
    }
}
