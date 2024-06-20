package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class UpdateReplyRequest {

    @Schema(hidden = true)
    Long memberNo;
    @Schema(hidden = true)
    Role role;
    @Schema(hidden = true)
    Long replyNo;
    @Schema(description = "변경할 댓글 내용", example = "변경된 댓글 내용")
    String replyContent;

}
