package com.runninghi.runninghibackv2.common.schedule;

import com.runninghi.runninghibackv2.domain.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreCleanUpBatch {

    private final ScoreRepository scoreRepository;

    @Scheduled(cron = "0 0 0 ? * 2")
    public void cleanUpWeeklyScore() {
        scoreRepository.deleteAll();
    }
}
