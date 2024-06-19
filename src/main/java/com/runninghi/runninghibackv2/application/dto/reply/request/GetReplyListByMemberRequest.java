package com.runninghi.runninghibackv2.application.dto.reply.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;


@Getter
@ToString
public class GetReplyListByMemberRequest {

    @PositiveOrZero(message = "현제 페이지는 0 또는 자연수만 입력 가능합니다.")
    @Schema(description = "현재 페이지", example = "1")
    int page;

    @Positive
    @Schema(description = "페이지 당 표시 댓글 수", example = "10")
    int size;

    @Positive(message = "memberNo는 자연수만 입력 가능합니다.")
    @Schema(description = "특정 회원 번호", example = "1")
    Long memberNo;

    Pageable pageable;

    public GetReplyListByMemberRequest(Integer page, Integer size, Long memberNo) {
        this.page = page == null ? 0 : page;
        this.size = size == null ? 10 : size;
        this.memberNo = memberNo;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
