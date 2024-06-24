package com.runninghi.runninghibackv2.application.dto.reply.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
public class GetReplyListRequest {

    @Positive(message = "현재 페이지는 자연수만 입력 가능합니다.")
    @Schema(description = "현재 페이지", example = "1")
    int page;

    @Positive
    @Schema(description = "페이지 당 표시 댓글 수", example = "10")
    int size;

    @Positive(message = "postNo는 자연수만 입력 가능합니다.")
    @Schema(description = "특정 게시물 번호", example = "1")
    Long postNo;

    Long memberNo;

    Pageable pageable;

    public GetReplyListRequest (Integer page, Integer size, Long postNo) {
        this.page = page == null ? 0 : page;
        this.size = size == null ? 10 : size;
        this.postNo = postNo;

    }
}
