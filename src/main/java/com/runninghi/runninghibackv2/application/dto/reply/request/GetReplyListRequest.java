package com.runninghi.runninghibackv2.application.dto.reply.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GetReplyListRequest {

    Long postNo;

    Long memberNo;

    Sort sort;

}
