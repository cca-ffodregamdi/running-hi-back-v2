package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetAllPostsResponse(
        @Schema(description = "게시글 번호", example = "1")
        Long postNo,
        @Schema(description = "작성자 닉네임", example = "러너1")
        String nickname,
        @Schema(description = "작성 날짜", example = "2024-05-22T14:33:29")
        LocalDateTime createDate,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "권한", example = "MEMBER")
        Role role,
        @Schema(description = "코스 위치", example = "서울특별시 성북구")
        String locationName,
        @Schema(description = "달린 거리(km)", example = "8.38")
        float distance,
        @Schema(description = "달린 시간", example = "1.23333")
        float time,
        @Schema(description = "평균 페이스 (분/km)", example = "4.66")
        float meanPace,
        @Schema(description = "이미지 URL 리스트", example = "[\"url1\", \"url2\"]")
        List<String> imageUrls
) {
    public static GetAllPostsResponse from(Post post, List<String> imageUrls) {
        return new GetAllPostsResponse(
                post.getPostNo(),
                post.getMember().getNickname(),
                post.getCreateDate(),
                post.getPostContent(),
                post.getRole(),
                post.getLocationName(),
                post.getGpsDataVO().getDistance(),
                post.getGpsDataVO().getTime(),
                post.getGpsDataVO().getMeanPace(),
                imageUrls
        );
    }
}
