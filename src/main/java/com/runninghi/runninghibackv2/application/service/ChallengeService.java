package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.challenge.request.CreateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.request.UpdateChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.challenge.response.CreateChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.challenge.response.DeleteChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.challenge.response.GetChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.challenge.response.UpdateChallengeResponse;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.repository.ChallengeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public CreateChallengeResponse createChallenge(CreateChallengeRequest request) {

        Challenge challenge = Challenge.builder()
                .title(request.title())
                .content(request.content())
                .distance(request.distance())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reward(request.reward())
                .build();

        challengeRepository.save(challenge);

        return CreateChallengeResponse.from(challenge);
    }

    @Transactional(readOnly = true)
    public List<GetChallengeResponse> getAllChallenges() {

        return challengeRepository.findAll().stream()
                .map(GetChallengeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetChallengeResponse getChallengeById(Long challengeNo) {

        Challenge challenge = challengeRepository.findById(challengeNo)
                .orElseThrow(EntityNotFoundException::new);

        return GetChallengeResponse.from(challenge);
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
