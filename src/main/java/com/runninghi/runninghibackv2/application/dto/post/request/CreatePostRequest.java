package com.runninghi.runninghibackv2.application.dto.post.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreatePostRequest(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "코스 난이도", example = "EASY")
        String difficulty,
        @Schema(description = "코스 위치", example = "서울특별시 성북구")
        String locationName,
        @Schema(description = "키워드 목록", example = "[\"보통\",\"강아지랑\",\"경사없음\"]")
        List<String> keywordList,
        @Schema(description = "이미지 목록", example = "[\"test.jpg\",\"test2.jpg2\"]")
        List<String> imageUrlList
) {

}
