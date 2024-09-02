package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.like.response.LikeResponse;
import com.runninghi.runninghibackv2.domain.entity.Like;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.vo.LikeId;
import com.runninghi.runninghibackv2.domain.enumtype.AlarmType;
import com.runninghi.runninghibackv2.domain.enumtype.TargetPage;
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
    private final AlarmService alarmService;

    private static final String LIKE_FCM_TITLE = "새로운 댓글이 도착했습니다.";

    @Transactional
    public LikeResponse createLike(Long memberNo, Long postNo) {

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);
        Post post = postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);
        Like like = Like.builder()
                .likeId(LikeId.of(memberNo, postNo))
                .member(member)
                .post(post)
                .build();

        likeRepository.save(like);

        // 포스트 작성자에게 알림
        CreateAlarmRequest alarmRequest = CreateAlarmRequest.builder()
                .title(LIKE_FCM_TITLE)
                .targetMemberNo(post.getMember().getMemberNo())
                .alarmType(AlarmType.LIKE)
                .targetPage(TargetPage.POST)
                .targetId(post.getPostNo())
                .fcmToken(member.getFcmToken())
                .build();
        alarmService.createPushAlarm(alarmRequest);


        return LikeResponse.of(likeRepository.countByPost_PostNo(postNo));
    }

    @Transactional
    public LikeResponse deleteLike(Long memberNo, Long postNo) {

        Like like = likeRepository.findById(LikeId.of(memberNo, postNo))
                .orElseThrow(EntityNotFoundException::new);
        likeRepository.delete(like);

        return LikeResponse.of(likeRepository.countByPost_PostNo(postNo));
    }
}
