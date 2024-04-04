package com.runninghi.runninghibackv2.post.application.dto.request;

import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreatePostRequest(
        @Schema(description = "회원 번호", example = "1")
        Long memberNo,
        @Schema(description = "권한", example = "MEMBER")
        Role role,
        @Schema(description = "게시글 제목", example = "제목 예시")
        String postTitle,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "코스 위치", example = "서울특별시 성북구")
        String locationName,
        @Schema(description = "키워드 목록", example = "보통,강아지랑,경사없음")
        List<String> keywordList
) {

}
