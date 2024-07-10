package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.challenge.request.CreateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.response.*;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.GetChallengeRankingResponse;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.GetMyChallengeResponse;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.repository.ChallengeQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.ChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    @Transactional
    public CreateChallengeResponse createChallenge(CreateChallengeRequest request) {

        Challenge challenge = Challenge.builder()
                .title(request.title())
                .content(request.content())
                .challengeCategory(request.challengeCategory())
                .imageUrl(request.imageUrl())
                .goal(request.goal())
                .goalDetail(request.goalDetail())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        challengeRepository.save(challenge);

        return CreateChallengeResponse.from(challenge);
    }

    @Transactional(readOnly = true)
    public List<GetAllChallengeResponse> getAllChallengesByStatus(boolean status, Long memberNo) {

        return challengeQueryRepository.findChallengesByStatusAndMember(status, memberNo).stream()
                .map(GetAllChallengeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChallengeResponse getChallengeById(Long challengeNo, Long memberNo) {

        Challenge challenge = challengeRepository.findById(challengeNo)
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByMemberNo(memberNo);

        List<GetChallengeRankingResponse> challengeRanking =
                memberChallengeRepository.findChallengeRanking(challenge.getChallengeNo());

        Optional<MemberChallenge> memberChallenge =
                memberChallengeRepository.findByMemberAndChallenge(member, challenge);

        if(memberChallenge.isPresent()) {
            GetChallengeRankingResponse memberRanking =
                    memberChallengeRepository.findMemberRanking(challengeNo, memberNo);

            return GetMyChallengeResponse.from(memberChallenge.get(), challengeRanking, memberRanking);
        }

        return GetChallengeResponse.from(challenge, challengeRanking);
    }

    @Transactional
    public UpdateChallengeResponse updateChallenge(Long challengeNo, UpdateChallengeRequest request) {

        Challenge challenge = challengeRepository.findById(challengeNo)
                .orElseThrow(EntityNotFoundException::new);

        challenge.update(request);

        return UpdateChallengeResponse.from(challenge);
    }

    @Transactional
    public DeleteChallengeResponse deleteChallenge(Long challengeNo) {

        challengeRepository.deleteById(challengeNo);

        return DeleteChallengeResponse.from(challengeNo);
    }
}
