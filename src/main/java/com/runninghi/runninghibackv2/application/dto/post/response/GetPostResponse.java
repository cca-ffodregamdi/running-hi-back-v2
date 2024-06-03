package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.entity.Keyword;
import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetPostResponse(

        @Schema(description = "작성자 닉네임", example = "러너1")
        String nickname,
        @Schema(description = "작성자 프로필 사진 URL", example = "https://picsum.photos/200")
        String profileImageUrl,
        @Schema(description = "작성자 레벨", example = "1")
        int level,
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
        @Schema(description = "소모 칼로리 (kcal)", example = "200")
        float kcal,
        @Schema(description = "이미지 URL 리스트", example = "[\"url1\", \"url2\"]")
        List<String> imageUrls
) {
    public static GetPostResponse from(Post post, List<String> imageUrls) {
        return new GetPostResponse(
                post.getMember().getNickname(),
                post.getMember().getProfileUrl(),
                post.getMember().getLevel(),
                post.getPostContent(),
                post.getRole(),
                post.getLocationName(),
                post.getGpsDataVO().getDistance(),
                post.getGpsDataVO().getTime(),
                post.getGpsDataVO().getMeanPace(),
                post.getGpsDataVO().getKcal(),
                imageUrls
        );
    }
}
