package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.common.response.PageResult;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.QImage;
import com.runninghi.runninghibackv2.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.runninghi.runninghibackv2.domain.entity.QImage.image;
import static com.runninghi.runninghibackv2.domain.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    @Transactional(readOnly = true)
    public PageResultData<GetAllPostsResponse> findAllPostsByPageable(Pageable pageable) {
        List<Post> posts;
        long total;

        total = jpaQueryFactory.selectFrom(post).fetchCount();
        posts = jpaQueryFactory.select(post)
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<GetAllPostsResponse> responses = posts.stream().map(post -> {
            Image mainImage = jpaQueryFactory.select(image)
                    .from(image)
                    .where(image.postNo.eq(post.getPostNo()))
                    .limit(1)
                    .fetchOne();

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;
            return GetAllPostsResponse.from(post, imageUrl);
        }).collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, total);
    }

    @Override
    public Page<GetAllPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo) {
        long total;

        List<Post> posts = jpaQueryFactory.select(post)
                .from(post)
                .where(post.member.memberNo.eq(memberNo))
                .fetch();
        total = jpaQueryFactory.selectFrom(post).fetchCount();

        List<GetAllPostsResponse> responses = posts.stream().map(post -> {
            Image mainImage = jpaQueryFactory.select(QImage.image)
                    .from(QImage.image)
                    .where(QImage.image.postNo.eq(post.getPostNo()))
                    .limit(1)
                    .fetchOne();

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;
            return GetAllPostsResponse.from(post, imageUrl);
        }).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, total);
    }

}
