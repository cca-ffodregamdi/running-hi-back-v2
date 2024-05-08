package com.runninghi.runninghibackv2.application.dto.post.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PostKeywordCriteria (
        @Schema(description = "페이지 번호", example = "0")
        int page,

        @Schema(description = "보여지는 게시글 개수", example = "10")
        int size,

        @Schema(description = "선택된 키워드(복수개 가능)", example = "[\"강아지랑\", \"초보자용\"]")
        List<String> keyword
){
}
