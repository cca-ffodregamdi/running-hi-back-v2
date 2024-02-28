package com.runninghi.runninghibackv2.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportCategory {

    INPROGRESS(0, "처리중"),
    DELETED(1, "삭제됨"),
    ;

    private final int value;
    private final String status;

}
