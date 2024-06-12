package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final TestDatabaseMapper testDatabaseMapper;

    @PostMapping("/test/data")
    public ResponseEntity<ApiResult<Void>> setUp() {

        testDatabaseMapper.disableForeignKeyChecks();
        List<String> tableList = testDatabaseMapper.getAllTableList();
        for (String i : tableList) {
            testDatabaseMapper.truncateTable(i);
        }
        testDatabaseMapper.enableForeignKeyChecks();

        testDatabaseMapper.insertMemberDummyData();
        testDatabaseMapper.insertPostDummyData();
        testDatabaseMapper.insertAlarmDummyData();
        testDatabaseMapper.insertFeedbackDummyData();
        testDatabaseMapper.insertImageDummyData();
        testDatabaseMapper.insertBookmarkDummyData();
        testDatabaseMapper.insertReplyDummyData();
        testDatabaseMapper.insertReportDummyData();

        return ResponseEntity.ok(ApiResult.success("DB 초기화, test용 dummy data 생성 성공", null));
    }

}
