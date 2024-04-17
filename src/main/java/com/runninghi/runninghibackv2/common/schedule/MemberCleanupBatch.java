package com.runninghi.runninghibackv2.common.schedule;

import com.runninghi.runninghibackv2.domain.entity.*;
import com.runninghi.runninghibackv2.domain.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class MemberCleanupBatch {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FeedbackRepository feedbackRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final ReplyReportRepository replyReportRepository;
    private final ReplyRepository replyRepository;
    private final TaskExecutor taskExecutor;
    private final EntityManager entityManager;

    private static final int BATCH_SIZE = 100;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupDeactivateMember() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Member> deactivateMembers = memberRepository.findByDeactivateDate(thirtyDaysAgo);

        for (Member deactivateMember : deactivateMembers) {
            taskExecutor.execute(() -> {
                deleteRelatedAlarms(deactivateMember);
                deleteRelatedFeedbacks(deactivateMember);
                deleteRelatedBookmarks(deactivateMember);
                deleteRelatedReplies(deactivateMember);
                deactivateMember.cleanupDeactivateMemberData();

                if (counter.incrementAndGet() % BATCH_SIZE == 0) {
                    entityManager.clear();
                }
            });

            deleteRelatedPosts(deactivateMember);
        }
    }

    private void deleteRelatedAlarms(Member deactivateMember) {
        alarmRepository.deleteAllByMember(deactivateMember);
    }

    private void deleteRelatedReplies(Member deactivateMember) {
        replyRepository.deleteAllByWriter(deactivateMember);
    }

    private void deleteRelatedBookmarks(Member deactivateMember) {
        bookmarkRepository.deleteAllByMember(deactivateMember);
    }

    private void deleteRelatedFeedbacks(Member deactivateMember) {
        feedbackRepository.deleteAllByFeedbackWriter(deactivateMember);
    }

    private void deleteRelatedPosts(Member deactivateMember) {
        List<Post> posts = postRepository.findAllByMember(deactivateMember);
        for (Post post : posts) {
            postKeywordRepository.deleteAllByPost(post);
            bookmarkRepository.deleteAllByPost(post);
            replyReportRepository.deleteAllByReportedReply_Post(post);
            replyRepository.deleteAllByPost(post);
            postReportRepository.deleteAllByReportedPost(post);
        }
        postRepository.deleteAllInBatch(posts);
    }

}
