package com.runninghi.runninghibackv2.application.dto.reply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReplyList {

        @Schema(description = "댓글 번호", example = "2")
        Long replyNo;
        @Schema(description = "댓글 작성자 번호", example = "1")
        Long memberNo;
        @Schema(description = "댓글 작성자 닉네임", example = "러너1")
        String memberName;
        @Schema(description = "프로필 이미지", example = "회원 프로필 사진")
        String profileUrl;
        @Schema(description = "게시글 번호", example = "1")
        Long postNo;
        @Schema(description = "댓글 내용", example = "댓글 내용")
        String replyContent;
        @Schema(description = "신고된 횟수", example = "1")
        int reportedCount;
        @Schema(description = "댓글 삭제 여부", example = "false")
        Boolean isDeleted;
        @Schema(description = "댓글 작성자 본인 여부", example = "true")
        Boolean isOwner = false;
        @Schema(description = "댓글 생성 일", example = "2024-03-27T13:23:12")
        LocalDateTime createDate;
        @Schema(description = "댓글 수정 여부", example = "false")
        Boolean isUpdated;

    public GetReplyList(Long replyNo, Long memberNo, String memberName, String profileUrl, Long postNo, String replyContent, int reportedCount, boolean isDeleted, LocalDateTime createDate, LocalDateTime updateDate) {
        this.replyNo = replyNo;
        this.memberNo = memberNo;
        this.memberName = memberName;
        this.profileUrl = profileUrl;
        this.postNo = postNo;
        this.replyContent = replyContent;
        this.reportedCount = reportedCount;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.isUpdated = updateDate != null;
    }

}
