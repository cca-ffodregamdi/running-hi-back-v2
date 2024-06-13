package com.runninghi.runninghibackv2.application.dto.member.response;

public record CreateMemberResponse(
    String memberNo
){
    public static CreateMemberResponse from(String memberNo) {
        return new CreateMemberResponse(memberNo);
    }
}
