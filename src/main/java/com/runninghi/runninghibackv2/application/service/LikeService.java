package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.like.response.CreateLikeResponse;
import com.runninghi.runninghibackv2.domain.entity.Like;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.vo.LikeId;
import com.runninghi.runninghibackv2.domain.repository.LikeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public CreateLikeResponse createLike(Long memberNo, Long postNo) {

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);
        Post post = postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);

        Like like = Like.builder()
                .likeId(LikeId.of(memberNo, postNo))
                .member(member)
                .post(post)
                .build();


        return CreateLikeResponse.fromEntity(likeRepository.save(like));
    }

    @Transactional
    public void deleteLike(Long memberNo, Long postNo) {

        Like like = likeRepository.findById(LikeId.of(memberNo, postNo))
                .orElseThrow(EntityNotFoundException::new);
        likeRepository.delete(like);
    }
}
