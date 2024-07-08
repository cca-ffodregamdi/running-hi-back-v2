package com.runninghi.runninghibackv2.application.dto.post.response;

import com.runninghi.runninghibackv2.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetRecordPostResponse {

        @Schema(description = "게시글 번호", example = "1")
        Long postNo;
        @Schema(description = "게시글 생성 시간", example = "2024-03-29T13:16:06")
        LocalDateTime createDate;
        @Schema(description = "코스 위치", example = "서울특별시 성북구")
        String locationName;
        @Schema(description = "달린 거리(km)", example = "8.38")
        float distance;
        @Schema(description = "달린 시간", example = "42000(초)")
        int time;
        @Schema(description = "이미지 URL 리스트", example = "https://picsum.photos/200")
        String imageUrl;
        @Schema(description = "공유 여부", example = "true")
        boolean status;
        @Schema(description = "코스 난이도", example = "EASY")
        String difficulty;

        public static GetRecordPostResponse from(Post post, String imageUrl) {
                return new GetRecordPostResponse(
                        post.getPostNo(),
                        post.getCreateDate(),
                        post.getLocationName(),
                        post.getGpsDataVO().getDistance(),
                        post.getGpsDataVO().getTime(),
                        imageUrl,
                        post.getStatus(),
                        post.getDifficulty().toString()
                );

        }
}
