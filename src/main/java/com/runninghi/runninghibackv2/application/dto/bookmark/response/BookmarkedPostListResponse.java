package com.runninghi.runninghibackv2.application.dto.bookmark.response;

import com.runninghi.runninghibackv2.domain.entity.Post;

public record BookmarkedPostListResponse(
        Long postNo,
        Long memberNo,
        String postContent,
        String role,
        String locationName,
        float distance,
        float time,
        float kcal,
        float meanPace,
        boolean isBookmarked

) {

    public static BookmarkedPostListResponse fromEntity (Post post) {
        return new BookmarkedPostListResponse(
                post.getPostNo(),
                post.getMember().getMemberNo(),
                post.getPostContent(),
                post.getGpsDataVO().getLocationName(),
                post.getRole().name(),
                post.getGpsDataVO().getDistance(),
                post.getGpsDataVO().getTime(),
                post.getGpsDataVO().getKcal(),
                post.getGpsDataVO().getMeanPace(),
                true
                );
    }
}
