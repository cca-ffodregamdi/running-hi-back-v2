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
        @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
        String postContent,
        @Schema(description = "권한", example = "MEMBER")
        Role role,
        @Schema(description = "코스 위치", example = "서울특별시 성북구")
        String locationName,
        @Schema(description = "코스 데이터", example = """
        {
          "startLatitude": 37.1234,
          "startLongitude": 127.5678,
          "endLatitude": 37.5678,
          "endLongitude": 127.1234,
          "distance": 10.5,
          "time": 3600,
          "kcal": 500,
          "speed": 2.5,
          "meanPace": 8.0,
          "meanSlope": 3.0
        }
        """)
        GpsDataVO gpsDataVO,
        @Schema(description = "키워드 목록", example = "보통,강아지랑,경사없음")
        List<Keyword> keywordList
) {
    public static GetPostResponse from(Post post, List<Keyword> list) {
        return new GetPostResponse(
                post.getMember().getNickname(),
                post.getPostContent(),
                post.getRole(),
                post.getLocationName(),
                post.getGpsDataVO(),
                list
        );
    }
}
