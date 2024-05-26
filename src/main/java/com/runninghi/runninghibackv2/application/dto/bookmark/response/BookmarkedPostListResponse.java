package com.runninghi.runninghibackv2.application.dto.bookmark.response;

import com.runninghi.runninghibackv2.domain.entity.Post;

public record BookmarkedPostListResponse(
        Long postNo,
        Long memberNo,
        String postContent,
        String role,
        String locationName,
        float startLatitude,
        float startLongitude,
        float endLatitude,
        float endLongitude,
        float distance,
        float time,
        float kcal,
        float speed,
        float meanPace,
        boolean isBookmarked

) {

    public static BookmarkedPostListResponse fromEntity (Post post) {
        return new BookmarkedPostListResponse(
                post.getPostNo(),
                post.getMember().getMemberNo(),
                post.getPostContent(),
                post.getLocationName(),
                post.getRole().name(),
                post.getGpsDataVO().getStartLatitude(),
                post.getGpsDataVO().getStartLongitude(),
                post.getGpsDataVO().getEndLatitude(),
                post.getGpsDataVO().getEndLongitude(),
                post.getGpsDataVO().getDistance(),
                post.getGpsDataVO().getTime(),
                post.getGpsDataVO().getKcal(),
                post.getGpsDataVO().getSpeed(),
                post.getGpsDataVO().getMeanPace(),
                true
                );
    }
}
