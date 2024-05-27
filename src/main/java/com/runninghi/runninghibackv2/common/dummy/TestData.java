package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.entity.*;
import com.runninghi.runninghibackv2.domain.entity.vo.BookmarkId;
import com.runninghi.runninghibackv2.domain.entity.vo.GpxDataVO;
import com.runninghi.runninghibackv2.domain.entity.vo.PostKeywordId;
import com.runninghi.runninghibackv2.domain.enumtype.FeedbackCategory;
import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.enumtype.ReportCategory;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestData {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FeedbackRepository feedbackRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final ReplyReportRepository replyReportRepository;
    private final ReplyRepository replyRepository;
    private final KeywordRepository keywordRepository;

    @PostMapping("/test/data")
    public ResponseEntity<ApiResult<Void>> setUp() {
        List<Member> members = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.now().minusDays(31);

        Member member1 = Member.builder()
                .deactivateDate(dateTime)
                .alarmConsent(true)
                .kakaoName("kakaoName")
                .level(1)
                .isActive(false)
                .totalDistance(1000)
                .build();
        members.add(member1);

        Member member2 = Member.builder()
                .deactivateDate(LocalDateTime.now().minusDays(15))
                .alarmConsent(true)
                .kakaoName("nyam")
                .level(1)
                .isActive(true)
                .totalDistance(1000)
                .build();
        members.add(member2);

        memberRepository.saveAllAndFlush(members);


        List<Alarm> alarms = new ArrayList<>();

        Alarm alarm1 = Alarm.builder()
                .member(member1)
                .title("테스트 알림 1")
                .content("테스트 알림 내용 1")
                .isRead(false)
                .createDate(LocalDateTime.now().minusDays(1))
                .build();
        alarms.add(alarm1);

        Alarm alarm2 = Alarm.builder()
                .member(member1)
                .title("테스트 알림 2")
                .content("테스트 알림 내용 2")
                .isRead(true)
                .createDate(LocalDateTime.now().minusDays(2))
                .readDate(LocalDateTime.now().minusDays(1))
                .build();
        alarms.add(alarm2);

        Alarm alarm3 = Alarm.builder()
                .member(member2)
                .title("테스트 알림 3")
                .content("테스트 알림 내용 3 : 남아있는 테스트 알림입니다.")
                .isRead(true)
                .createDate(LocalDateTime.now().minusDays(2))
                .readDate(LocalDateTime.now().minusDays(1))
                .build();
        alarms.add(alarm3);

        alarmRepository.saveAllAndFlush(alarms);

        List<Post> posts = new ArrayList<>();

        Post post1 = Post.builder()
                .member(member1)
                .postTitle("첫 번째 게시글")
                .postContent("첫 번째 게시글 내용입니다.")
                .role(Role.USER)
                .locationName("서울")
                .gpxDataVO(new GpxDataVO(37.1234f, 127.5678f, 37.9876f, 126.5432f, 10.5f, 3600f, 200f, 5f, 6f))
                .build();
        posts.add(post1);

        // 남아있는 게시물
        Post post2 = Post.builder()
                .member(member2)
                .postTitle("두 번째 게시글")
                .postContent("두 번째 게시글 내용입니다. : 남아있는 게시글 입니다.")
                .role(Role.ADMIN)
                .locationName("부산")
                .gpxDataVO(new GpxDataVO(36.9876f, 126.5432f, 36.1234f, 127.5678f, 12.3f, 4500f, 250f, 4f, 5f))
                .build();
        posts.add(post2);

        Post post3 = Post.builder()
                .member(member1)
                .postTitle("세 번째 게시글")
                .postContent("세 번째 게시글 내용입니다.")
                .role(Role.USER)
                .locationName("대구")
                .gpxDataVO(new GpxDataVO(35.6789f, 128.9876f, 35.4321f, 129.8765f, 8.7f, 3000f, 180f, 6f, 7f))
                .build();
        posts.add(post3);

        postRepository.saveAllAndFlush(posts);


        List<Bookmark> bookmarks = new ArrayList<>();

        Bookmark bookmark1 = new Bookmark.BookmarkBuilder()
                .bookmarkId(new BookmarkId(1L, 1L))
                .member(member1)
                .post(post1)
                .build();
        bookmarks.add(bookmark1);

        Bookmark bookmark2 = new Bookmark.BookmarkBuilder()
                .bookmarkId(new BookmarkId(1L, 2L))
                .member(member1)
                .post(post2)
                .build();
        bookmarks.add(bookmark2);

        Bookmark bookmark3 = new Bookmark.BookmarkBuilder()
                .bookmarkId(new BookmarkId(2L, 3L))
                .member(member2)
                .post(post3)
                .build();
        bookmarks.add(bookmark3);

        // 남아있는 북마크
        Bookmark bookmark4 = new Bookmark.BookmarkBuilder()
                .bookmarkId(new BookmarkId(2L, 2L))
                .member(member2)
                .post(post2)
                .build();
        bookmarks.add(bookmark4);

        bookmarkRepository.saveAllAndFlush(bookmarks);


        List<Reply> replies = new ArrayList<>();

        Reply reply1 = Reply.builder()
                .writer(member1)
                .post(post1)
                .replyContent("첫 번째 댓글 내용입니다. : member1")
                .reportedCount(1)
                .isDeleted(false)
                .build();
        replies.add(reply1);

        // 남아있는 댓글
        Reply reply2 = Reply.builder()
                .writer(member2)
                .post(post2)
                .replyContent("두 번째 댓글 내용입니다. : 남아있는 댓글입니다.")
                .reportedCount(1)
                .isDeleted(false)
                .build();
        replies.add(reply2);

        Reply reply3 = Reply.builder()
                .writer(member2)
                .post(post1)
                .replyContent("첫 번째 댓글 내용입니다. : member2")
                .reportedCount(1)
                .isDeleted(false)
                .build();
        replies.add(reply3);

        Reply parentReply = Reply.builder()
                .writer(member1)
                .post(post2)
                .replyContent("부모 댓글입니다.")
                .reportedCount(2)
                .isDeleted(false)
                .build();
        replies.add(parentReply);

        Reply childReply1 = Reply.builder()
                .writer(member2)
                .post(post2)
                .replyContent("자식 댓글입니다.")
                .reportedCount(1)
                .isDeleted(false)
                .parent(parentReply)
                .build();
        replies.add(childReply1);

        Reply childReply2 = Reply.builder()
                .writer(member1)
                .post(post2)
                .replyContent("또 다른 자식 댓글입니다.")
                .reportedCount(1)
                .isDeleted(false)
                .parent(parentReply)
                .build();
        replies.add(childReply2);

        replyRepository.saveAllAndFlush(replies);


        List<ReplyReport> replyReports = new ArrayList<>();

        // 남아있는 댓글 신고
        ReplyReport report1 = ReplyReport.builder()
                .category(ReportCategory.SPAM)
                .content("스팸 댓글입니다.")
                .status(ProcessingStatus.INPROGRESS)
                .reporter(member1)
                .reportedReply(reply2)
                .replyContent(reply2.getReplyContent())
                .isReplyDeleted(false)
                .build();
        replyReports.add(report1);
        reply2.addReplyReport(report1);

        ReplyReport report2 = ReplyReport.builder()
                .category(ReportCategory.ILLEGALITY)
                .content("부적절한 내용이 포함된 댓글입니다.")
                .status(ProcessingStatus.INPROGRESS)
                .reporter(member2)
                .reportedReply(reply1)
                .replyContent(reply1.getReplyContent())
                .isReplyDeleted(false)
                .build();
        replyReports.add(report2);
        reply1.addReplyReport(report2);

        ReplyReport report3 = ReplyReport.builder()
                .category(ReportCategory.ILLEGALITY)
                .content("부적절한 내용이 포함된 댓글입니다.")
                .status(ProcessingStatus.INPROGRESS)
                .reporter(member2)
                .reportedReply(childReply1)
                .replyContent(childReply1.getReplyContent())
                .isReplyDeleted(false)
                .build();
        replyReports.add(report3);
        childReply1.addReplyReport(report3);

        replyReportRepository.saveAllAndFlush(replyReports);


        List<PostReport> postReports = new ArrayList<>();

        PostReport postReport1 = PostReport.builder()
                .category(ReportCategory.SPAM)
                .content("스팸 게시글입니다.")
                .status(ProcessingStatus.INPROGRESS)
                .reporter(member2)
                .reportedPost(post1)
                .postContent(post1.getPostContent())
                .isPostDeleted(false)
                .build();
        postReports.add(postReport1);

        // 남아있는 게시물 신고
        PostReport postReport2 = PostReport.builder()
                .category(ReportCategory.ILLEGALITY)
                .content("부적절한 내용이 포함된 게시글입니다.")
                .status(ProcessingStatus.INPROGRESS)
                .reporter(member1)
                .reportedPost(post2)
                .postContent(post2.getPostContent())
                .isPostDeleted(false)
                .build();
        postReports.add(postReport2);

        // 남아있는 게시물 신고
        PostReport postReport3 = PostReport.builder()
                .category(ReportCategory.ILLEGALITY)
                .content("부적절한 내용이 포함된 게시글입니다.")
                .status(ProcessingStatus.INPROGRESS)
                .reporter(member2)
                .reportedPost(post2)
                .postContent(post2.getPostContent())
                .isPostDeleted(false)
                .build();
        postReports.add(postReport3);

        postReportRepository.saveAllAndFlush(postReports);


        List<Keyword> keywords = new ArrayList<>();
        Keyword keyword1 = new Keyword("Hello");
        keywords.add(keyword1);
        Keyword keyword2 = new Keyword("World");
        keywords.add(keyword2);
        Keyword keyword3 = new Keyword("Nice");
        keywords.add(keyword3);

        keywordRepository.saveAllAndFlush(keywords);


        List<PostKeyword> postKeywords = new ArrayList<>();

        PostKeyword postKeyword1 = PostKeyword.builder()
                .postKeywordId(PostKeywordId.builder()
                        .keywordNo(keyword1.getKeywordNo())
                        .postNo(post1.getPostNo())
                        .build())
                .keyword(keyword1)
                .post(post1)
                .build();
        postKeywords.add(postKeyword1);

        PostKeyword postKeyword2 = PostKeyword.builder()
                .postKeywordId(PostKeywordId.builder()
                        .keywordNo(keyword2.getKeywordNo())
                        .postNo(post2.getPostNo())
                        .build())
                .keyword(keyword2)
                .post(post2)
                .build();
        postKeywords.add(postKeyword2);

        postKeywordRepository.saveAllAndFlush(postKeywords);


        List<Feedback> feedbacks = new ArrayList<>();

        Feedback feedback1 = Feedback.builder()
                .title("첫 번째 피드백")
                .content("첫 번째 피드백 내용입니다.")
                .hasReply(false)
                .reply(null)
                .category(FeedbackCategory.PROPOSAL)
                .feedbackWriter(member1)
                .build();
        feedbacks.add(feedback1);

        Feedback feedback2 = Feedback.builder()
                .title("두 번째 피드백")
                .content("두 번째 피드백 내용입니다.")
                .hasReply(true)
                .reply("두 번째 피드백에 대한 답변입니다.")
                .category(FeedbackCategory.INQUIRY)
                .feedbackWriter(member2)
                .build();
        feedbacks.add(feedback2);

        feedbackRepository.saveAllAndFlush(feedbacks);

        return ResponseEntity.ok(ApiResult.success("test용 dummy data 생성 성공", null));
    }

}
