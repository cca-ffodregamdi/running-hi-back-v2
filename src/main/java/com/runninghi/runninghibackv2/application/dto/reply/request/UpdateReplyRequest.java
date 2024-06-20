package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "수정할 댓글을 입력해주세요.")
    @Schema(description = "변경할 댓글 내용", example = "변경된 댓글 내용")
    String replyContent;

}
