package com.runninghi.runninghibackv2.application.dto.alarm.request;

import com.runninghi.runninghibackv2.domain.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyFCMRequest {

        Reply savedReply;
        Reply parentReply;

}
