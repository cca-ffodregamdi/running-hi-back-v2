package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetMyPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetPostResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetRecordPostResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PostQueryRepository {
    PageResultData<GetMyPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo);
    GetPostResponse getPostDetailByPostNo(Long memberNo, Long postNo);
    PageResultData<GetAllPostsResponse> findAllPostsByLatest(Long memberNo, Pageable pageable, int distance);
    PageResultData<GetAllPostsResponse> findAllPostsByRecommended(Long memberNo, Pageable pageable, int distance);
    PageResultData<GetAllPostsResponse> findAllPostsByLikeCnt(Long memberNo, Pageable pageable, int distance);
    PageResultData<GetAllPostsResponse> findAllPostsByDistance(Long memberNo, Pageable pageable, int distance);
    PageResultData<GetAllPostsResponse> findMyLikedPostsByPageable(Pageable pageable, Long memberNo);
    PageResultData<GetAllPostsResponse> findMyBookmarkedPostsByPageable(Pageable pageable, Long memberNo);
    List<GetRecordPostResponse> findWeeklyRecord(Long memberNo, LocalDate date);
    List<GetRecordPostResponse> findMonthlyRecord(Long memberNo, LocalDate date);
    List<GetRecordPostResponse> findYearlyRecord(Long memberNo, LocalDate date);
}
