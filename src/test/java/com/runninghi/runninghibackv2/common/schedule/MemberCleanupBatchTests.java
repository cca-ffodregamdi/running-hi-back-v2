package com.runninghi.runninghibackv2.common.schedule;

import com.runninghi.runninghibackv2.domain.entity.Alarm;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.vo.GpxDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class MemberCleanupBatchTests {

    @Autowired
    private MemberCleanupBatch memberCleanupBatch;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyReportRepository replyReportRepository;

    @Autowired
    private ReplyRepository replyRepository;

    static Member member1;
    static Member member2;

    @BeforeEach
    void setUp() {
        List<Member> members = new ArrayList<>();

        member1 = Member.builder()
                .deactivateDate(LocalDateTime.now().minusDays(30))
                .build();
        members.add(member1);

        member2 = Member.builder()
                .deactivateDate(LocalDateTime.now().minusDays(32))
                .build();
        members.add(member2);

        memberRepository.saveAll(members);

        List<Alarm> alarms = new ArrayList<>();

        Alarm alarm1 = Alarm.builder()
                .id(1L)
                .member(member1)
                .title("테스트 알림 1")
                .content("테스트 알림 내용 1")
                .isRead(false)
                .createDate(LocalDateTime.now().minusDays(1))
                .build();
        alarms.add(alarm1);

        Alarm alarm2 = Alarm.builder()
                .id(2L)
                .member(member1)
                .title("테스트 알림 2")
                .content("테스트 알림 내용 2")
                .isRead(true)
                .createDate(LocalDateTime.now().minusDays(2))
                .readDate(LocalDateTime.now().minusDays(1))
                .build();
        alarms.add(alarm2);

        alarmRepository.saveAll(alarms);

        List<Post> posts = new ArrayList<>();

        Post firstPost = Post.builder()
                .member(member1)
                .postTitle("첫 번째 게시글")
                .postContent("첫 번째 게시글 내용입니다.")
                .role(Role.USER)
                .locationName("서울")
                .gpxDataVO(new GpxDataVO(37.1234f, 127.5678f, 37.9876f, 126.5432f, 10.5f, 3600f, 200f, 5f, 6f, 3f))
                .build();
        posts.add(firstPost);

        Post secondPost = Post.builder()
                .member(member1)
                .postTitle("두 번째 게시글")
                .postContent("두 번째 게시글 내용입니다.")
                .role(Role.ADMIN)
                .locationName("부산")
                .gpxDataVO(new GpxDataVO(36.9876f, 126.5432f, 36.1234f, 127.5678f, 12.3f, 4500f, 250f, 4f, 5f, 2f))
                .build();
        posts.add(secondPost);

        Post thirdPost = Post.builder()
                .member(member1)
                .postTitle("세 번째 게시글")
                .postContent("세 번째 게시글 내용입니다.")
                .role(Role.USER)
                .locationName("대구")
                .gpxDataVO(new GpxDataVO(35.6789f, 128.9876f, 35.4321f, 129.8765f, 8.7f, 3000f, 180f, 6f, 7f, 4f))
                .build();
        posts.add(thirdPost);

        postRepository.saveAll(posts);
    }

    @Test
    void cleanupDeactivateMemberTest() {
        List<Post> posts = postRepository.findAllByMember(member1);
        int postsCount = posts.size();

        memberCleanupBatch.cleanupDeactivateMember();

        List<Member> deactivatedMembers = memberRepository.findByDeactivateDate(LocalDateTime.now().minusDays(30));
        List<Post> deletedPosts = postRepository.findAllByMember(member1);

        assertNotEquals(0, postsCount);
        assertTrue(deactivatedMembers.isEmpty());
//        assertTrue(deletedPosts.isEmpty());
    }
}