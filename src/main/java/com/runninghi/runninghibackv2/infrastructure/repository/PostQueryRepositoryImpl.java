package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetMyPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetPostResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.QImage;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyRepository;
import com.runninghi.runninghibackv2.domain.service.PostChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.runninghi.runninghibackv2.domain.entity.QBookmark.bookmark;
import static com.runninghi.runninghibackv2.domain.entity.QImage.image;
import static com.runninghi.runninghibackv2.domain.entity.QLike.like;
import static com.runninghi.runninghibackv2.domain.entity.QPost.post;
import static com.runninghi.runninghibackv2.domain.entity.QReply.reply;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final PostChecker postChecker;
    private final MemberRepository memberRepository;

    private static final String DISTANCE_CONDITION = "ST_Distance_Sphere({0}, {1}) <= {2}";


    @Override
    public PageResultData<GetMyPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo) {
        long total;

        List<Post> posts = jpaQueryFactory.select(post)
                .from(post)
                .where(post.member.memberNo.eq(memberNo))
                .fetch();
        total = jpaQueryFactory.selectFrom(post).fetchCount();

        List<GetMyPostsResponse> responses = posts.stream().map(post -> {
            Image mainImage = jpaQueryFactory.select(QImage.image)
                    .from(QImage.image)
                    .where(QImage.image.postNo.eq(post.getPostNo()))
                    .limit(1)
                    .fetchOne();

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;

            return GetMyPostsResponse.from(post, imageUrl);
        }).collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, total);
    }

    @Override
    public GetPostResponse getPostDetailByPostNo(Long memberNo, Long postNo) {

        Post post = postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);

        Boolean isWriter = postChecker.isOwner(memberNo, post.getMember().getMemberNo());

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



        return GetPostResponse.from(post, imageUrl, bookmarkCnt, replyCnt, isWriter);
    }

    @Override
    public PageResultData<GetAllPostsResponse> findAllPostsByLatest(Long memberNo, Pageable pageable, int distance) {
        List<Post> posts;
        long total;

        Point referencePoint = memberRepository.findByMemberNo(memberNo).getGeometry();

        total = jpaQueryFactory.selectFrom(post)
                .where(post.status.eq(true)
                        .and(post.geometry.isNotNull())
                        .and(Expressions.booleanTemplate(
                                DISTANCE_CONDITION,
                                post.geometry, referencePoint, distance * 10000
                        ))
                )
                .fetchCount();

        posts = jpaQueryFactory.selectFrom(post)
                .where(post.status.eq(true)
                        .and(post.geometry.isNotNull())
                        .and(Expressions.booleanTemplate(
                                DISTANCE_CONDITION,
                                post.geometry, referencePoint, distance * 10000
                        ))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createDate.desc())
                .fetch();

        List<GetAllPostsResponse> responses = posts.stream().map(post -> {
            Image mainImage = jpaQueryFactory.selectFrom(image)
                    .where(image.postNo.eq(post.getPostNo()))
                    .limit(1)
                    .fetchOne();

            Long replyCnt = jpaQueryFactory.select(reply.count())
                    .from(reply)
                    .where(reply.post.postNo.eq(post.getPostNo()))
                    .fetchOne();

            Long likeCnt = jpaQueryFactory.select(like.count())
                    .from(like)
                    .where(like.post.postNo.eq(post.getPostNo()))
                    .fetchOne();

            Boolean isBookmarked = jpaQueryFactory.selectFrom(bookmark)
                    .where(bookmark.member.memberNo.eq(memberNo)
                            .and(bookmark.post.postNo.eq(post.getPostNo())))
                    .fetchFirst() != null;

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;
            return GetAllPostsResponse.from(post, imageUrl, replyCnt, likeCnt, isBookmarked);
        }).collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, total);
    }

    @Override
    public PageResultData<GetAllPostsResponse> findAllPostsByRecommended(Long memberNo, Pageable pageable, int distance) {
        List<Post> posts;
        long total;

        Point referencePoint = memberRepository.findByMemberNo(memberNo).getGeometry();

        total = jpaQueryFactory.selectFrom(post)
                .where(post.status.eq(true)
                        .and(Expressions.booleanTemplate(
                                DISTANCE_CONDITION,
                                post.geometry, referencePoint, distance * 10000
                        ))
                )
                .fetchCount();

        posts = jpaQueryFactory.select(post)
                .from(post)
                .leftJoin(bookmark).on(bookmark.post.postNo.eq(post.postNo))
                .where(post.status.eq(true)
                        .and(Expressions.booleanTemplate(
                                DISTANCE_CONDITION,
                                post.geometry, referencePoint, distance * 10000
                        ))
                )
                .groupBy(post.postNo)
                .orderBy(
                    Expressions.numberTemplate(Long.class, "count({0})", bookmark.post.postNo).desc(),
                    post.createDate.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<GetAllPostsResponse> responses = posts.stream().map(post -> {
            Image mainImage = jpaQueryFactory.selectFrom(image)
                    .where(image.postNo.eq(post.getPostNo()))
                    .limit(1)
                    .fetchOne();

            Long replyCnt = jpaQueryFactory.select(reply.count())
                    .from(reply)
                    .where(reply.post.postNo.eq(post.getPostNo()))
                    .fetchOne();

            Long likeCnt = jpaQueryFactory.select(like.count())
                    .from(like)
                    .where(like.post.postNo.eq(post.getPostNo()))
                    .fetchOne();

            Boolean isBookmarked = jpaQueryFactory.selectFrom(bookmark)
                    .where(bookmark.member.memberNo.eq(memberNo)
                            .and(bookmark.post.postNo.eq(post.getPostNo())))
                    .fetchFirst() != null;

            String imageUrl = mainImage != null ? mainImage.getImageUrl() : null;
            return GetAllPostsResponse.from(post, imageUrl, replyCnt, likeCnt, isBookmarked);
        }).collect(Collectors.toList());

        return new PageResultData<>(responses, pageable, total);
    }



}
