package com.runninghi.runninghibackv2.bookmark.application.dto.response;

import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public record BookmarkedPostListResponse(
        Long postNo,
        Long memberNo,
        String postTitle,
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
        float meanSlope,
        boolean isBookmarked

) {

    public static BookmarkedPostListResponse convertToDTO (Post post) {
        return new BookmarkedPostListResponse(
                post.getPostNo(),
                post.getMember().getMemberNo(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getLocationName(),
                post.getRole().name(),
                post.getGpxDataVO().getStartLatitude(),
                post.getGpxDataVO().getStartLongitude(),
                post.getGpxDataVO().getEndLatitude(),
                post.getGpxDataVO().getEndLongitude(),
                post.getGpxDataVO().getDistance(),
                post.getGpxDataVO().getTime(),
                post.getGpxDataVO().getKcal(),
                post.getGpxDataVO().getSpeed(),
                post.getGpxDataVO().getMeanPace(),
                post.getGpxDataVO().getMeanSlope(),
                true
                );
    }
}
