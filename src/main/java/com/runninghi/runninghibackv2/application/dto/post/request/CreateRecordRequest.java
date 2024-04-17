package com.runninghi.runninghibackv2.application.dto.post.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateRecordRequest(
        @Schema(description = "회원 번호", example = "1")
        Long memberNo,
        @Schema(description = "권한", example = "MEMBER")
        Role role,
        @Schema(description = "코스 위치", example = "서울특별시 성북구")
        String locationName
) {

}
