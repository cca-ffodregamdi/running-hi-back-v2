package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetPostResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.QImage;
import com.runninghi.runninghibackv2.domain.repository.PostQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.runninghi.runninghibackv2.domain.entity.QBookmark.bookmark;
import static com.runninghi.runninghibackv2.domain.entity.QImage.image;
import static com.runninghi.runninghibackv2.domain.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResultData<GetAllPostsResponse> findAllPostsByPageable(Pageable pageable) {
        List<Post> posts;
        long total;

        total = jpaQueryFactory.selectFrom(post).fetchCount();
        posts = jpaQueryFactory.select(post)
                .from(post)
                .where(post.status.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<GetAllPostsResponse> responses = posts.stream().map(post -> {
            Image mainImage = jpaQueryFactory.select(image)
                    .from(image)
                    .where(image.postNo.eq(post.getPostNo()))
                    .limit(1)
                    .fetchOne();

            Long bookmarkCnt = jpaQueryFactory.select(bookmark.count())
                    .from(bookmark)
                    .where(bookmark.post.postNo.eq(post.getPostNo()))
                    .fetchOne();

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;
            return GetAllPostsResponse.from(post, imageUrl, bookmarkCnt);
        }).collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, total);
    }

    @Override
    public PageResultData<GetAllPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo) {
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

            Long bookmarkCnt = jpaQueryFactory.select(bookmark.count())
                    .from(bookmark)
                    .where(bookmark.post.postNo.eq(post.getPostNo()))
                    .fetchOne();

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;

            return GetAllPostsResponse.from(post, imageUrl, bookmarkCnt);
        }).collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, total);
    }

    @Override
    public GetPostResponse getPostDetailByPostNo(Long postNo) {

        Post post = postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);

        String imageUrl = jpaQueryFactory
                .select(image.imageUrl)
                .from(image)
                .where(image.postNo.eq(postNo))
                .fetchFirst();

        Long bookmarkCnt = jpaQueryFactory
                .select(bookmark.count())
                .from(bookmark)
                .where(bookmark.post.postNo.eq(postNo))
                .fetchOne();

        Long replyCnt = replyRepository.countByPost_PostNo(postNo);



        return GetPostResponse.from(post, imageUrl, bookmarkCnt, replyCnt);
    }

}
