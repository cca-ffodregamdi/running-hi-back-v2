package com.runninghi.runninghibackv2.common.schedule;

import com.runninghi.runninghibackv2.common.exception.custom.SchedulingException;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
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

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupDeactivateMember() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Member> deactivateMembers = memberRepository.findAllByDeactivateDate(thirtyDaysAgo);

        if (!deactivateMembers.isEmpty()) {
            for (Member deactivateMember : deactivateMembers) {
                deleteRelatedAlarms(deactivateMember);
                deleteRelatedFeedbacks(deactivateMember);
                deleteRelatedBookmarks(deactivateMember);
                deleteRelatedReplies(deactivateMember);
                deleteRelatedPosts(deactivateMember);
                deactivateMember.cleanupDeactivateMemberData();
            }
        }
    }

    private void deleteRelatedAlarms(Member deactivateMember) {
        try {
            alarmRepository.deleteAllByMember_MemberNo(deactivateMember.getMemberNo());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchedulingException();
        }
    }

    private void deleteRelatedReplies(Member deactivateMember) {
        try {
            replyRepository.deleteAllByWriter(deactivateMember);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchedulingException();
        }
    }

    private void deleteRelatedBookmarks(Member deactivateMember) {
        try {
            bookmarkRepository.deleteAllByMember(deactivateMember);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchedulingException();
        }
    }

    private void deleteRelatedFeedbacks(Member deactivateMember) {
        try {
            feedbackRepository.deleteAllByFeedbackWriter(deactivateMember);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchedulingException();
        }
    }

    private void deleteRelatedPosts(Member deactivateMember) {
        try {
            List<Post> posts = postRepository.findAllByMember(deactivateMember);
            if (!posts.isEmpty()) {
                for (Post post : posts) {
                    postKeywordRepository.deleteAllByPost(post);
                    bookmarkRepository.deleteAllByPost(post);
                    replyReportRepository.deleteAllByReportedReply_Post(post);
                    replyRepository.deleteAllByPost(post);
                    postReportRepository.deleteAllByReportedPost(post);
                }
                postRepository.deleteAllInBatch(posts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchedulingException();
        }
    }

}