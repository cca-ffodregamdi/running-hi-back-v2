package com.runninghi.runninghibackv2.application.dto.member.response;

public record GetMemberNoResponse(
        Long memberNo
){
    public static GetMemberNoResponse from(Long memberNo) {
        return new GetMemberNoResponse(memberNo);
    }
}
