package com.runninghi.runninghibackv2.application.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.runninghi.runninghibackv2.application.dto.alarm.ReplyFCMDTO;
import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.alarm.response.GetAllAlarmResponse;
import com.runninghi.runninghibackv2.domain.entity.Alarm;
import com.runninghi.runninghibackv2.domain.entity.Member;
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
     * @param replyFCMDTO 저장된 댓글
     */
    public void sendReplyPushNotification(ReplyFCMDTO replyFCMDTO) {

        List<Alarm> alarmList = new ArrayList<>();

        Notification notification = Notification.builder()
                .setTitle(POST_FCM_TITLE)
                .setBody(POST_FCM_MESSAGE)
                .build();

        Message message = Message.builder()
                .setToken(replyFCMDTO.getSavedReply().getMember().getFcmToken())
                .setNotification(notification)
                .build();

        ApiFuture<String> stringApiFuture = firebaseMessaging.sendAsync(message);
        System.out.println("stringApiFuture : " + stringApiFuture); // 알림 전송 결과 로깅 필요

        // 게시글 알림 추가
        alarmList.add(
                Alarm.builder()
                        .member(replyFCMDTO.getSavedReply().getPost().getMember())
                        .title(POST_FCM_TITLE)
                        .content(POST_FCM_MESSAGE)
                        .build()
        );

        alarmRepository.saveAll(alarmList);

    }


    public List<GetAllAlarmResponse> getAllAlarm(Long memberNo) {

        List<Alarm> alarmList = alarmRepository.findAllByMember_MemberNo(memberNo);
        return alarmList.stream()
                .map(Alarm::toResponse)
                .toList();
    }

    public void createAlarm(CreateAlarmRequest request, Long memberNo) throws FirebaseMessagingException {

        Member member = memberRepository.findByMemberNo(memberNo);
        Alarm alarm = Alarm.builder()
                .member(member)
                .title(request.title())
                .content(request.content())
                .build();

        alarmRepository.save(alarm);

        sendAlarm(request, member.getFcmToken());
    }

    private void sendAlarm(CreateAlarmRequest request, String fcmToken) throws FirebaseMessagingException {

        Notification notification = Notification.builder()
                        .setTitle(request.title())
                        .setBody(request.content())
                        .build();
        Message message = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(notification)
                        .build();
        firebaseMessaging.send(message);
    }
}
