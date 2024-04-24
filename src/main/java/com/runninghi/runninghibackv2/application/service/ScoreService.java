package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.score.GetRankingResponse;
import com.runninghi.runninghibackv2.domain.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public List<GetRankingResponse> getAllRanking() {
        return scoreRepository.findAllRanking();
    }

    public GetRankingResponse getMemberRanking(Long memberNo) {
        return scoreRepository.findMemberRanking(memberNo);
    }
}
