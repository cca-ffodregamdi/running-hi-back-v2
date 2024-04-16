package com.runninghi.runninghibackv2.application.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.runninghi.runninghibackv2.application.dto.alarm.request.ReplyFCMRequest;
import com.runninghi.runninghibackv2.domain.entity.Alarm;
import com.runninghi.runninghibackv2.domain.repository.AlarmRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final FirebaseMessaging firebaseMessaging;

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    private final String POST_FCM_MESSAGE = "회원님의 게시글에 댓글이 등록되었습니다.";
    private final String POST_FCM_TITLE = "댓글 알림";

    /**
     * 댓글 관련 푸쉬 알림 발송 메소드
     * 댓글 작성 시, 게시글 작성자에게 알림 발송.
     * 대댓글 작성 시, 게시글 작성자, 부모 댓글 작성자에게 알림 발송.
     * @param replyFCMRequest 저장된 댓글, 부모 댓글
     */
    public void sendReplyPushNotification(ReplyFCMRequest replyFCMRequest) {

        List<Alarm> alarmList = new ArrayList<>();

        Notification notification = Notification.builder()
                .setTitle(POST_FCM_TITLE)
                .setBody(POST_FCM_MESSAGE)
                .build();

        Message message = Message.builder()
                .setToken(replyFCMRequest.getSavedReply().getWriter().getFcmToken())
                .setNotification(notification)
                .build();

        ApiFuture<String> stringApiFuture = firebaseMessaging.sendAsync(message);
        System.out.println("stringApiFuture : " + stringApiFuture); // 알림 전송 결과 로깅 필요

        // 게시글 알림 추가
        alarmList.add(
                Alarm.builder()
                        .member(replyFCMRequest.getSavedReply().getPost().getMember())
                        .title(POST_FCM_TITLE)
                        .content(POST_FCM_MESSAGE)
                        .build()
        );

        // 부모 댓글 대댓글 작성 시
        if (replyFCMRequest.getParentReply() != null) {

            Notification childReplyNotification = Notification.builder()
                    .setTitle(replyFCMRequest.getSavedReply().getWriter().getNickname())
                    .setBody(replyFCMRequest.getParentReply().getReplyContent())
                    .build();

            Message childReplyMessage = Message.builder()
                    .setToken(replyFCMRequest.getParentReply().getWriter().getFcmToken())
                    .setNotification(childReplyNotification)
                    .build();

            firebaseMessaging.sendAsync(childReplyMessage);
            alarmList.add(
                    Alarm.builder()
                            .member(replyFCMRequest.getParentReply().getWriter())
                            .title(replyFCMRequest.getSavedReply().getWriter().getNickname())
                            .content(replyFCMRequest.getSavedReply().getReplyContent())
                            .build()
            );
        }

        alarmRepository.saveAll(alarmList);

    }


}
