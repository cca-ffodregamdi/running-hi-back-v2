package com.runninghi.runninghibackv2.application.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.runninghi.runninghibackv2.application.dto.alarm.ReplyFCMDTO;
import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.alarm.response.GetAllAlarmResponse;
import com.runninghi.runninghibackv2.domain.entity.Alarm;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.enumtype.AlarmType;
import com.runninghi.runninghibackv2.domain.enumtype.TargetPage;
import com.runninghi.runninghibackv2.domain.repository.AlarmRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final FirebaseMessaging firebaseMessaging;

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    private final String POST_REPLY_FCM_TITLE = "회원님의 게시글에 댓글이 등록되었습니다.";

    /**
     * 댓글 관련 푸쉬 알림 발송 메소드
     * 댓글 작성 시, 게시글 작성자에게 알림 발송.
     * @param replyFCMDTO 저장된 댓글
     */
    public void sendReplyPushNotification(ReplyFCMDTO replyFCMDTO) throws FirebaseMessagingException {

        CreateAlarmRequest alarmRequest = CreateAlarmRequest.of(
                POST_REPLY_FCM_TITLE,
                replyFCMDTO.getSavedReply().getMember().getMemberNo(),
                AlarmType.REPLY,
                TargetPage.POST,
                replyFCMDTO.getSavedReply().getReplyNo()
        );

        createAlarm(alarmRequest);
        sendAlarm(alarmRequest, replyFCMDTO.getSavedReply().getMember().getFcmToken());
    }


    @Transactional(readOnly = true)
    public List<GetAllAlarmResponse> getAllAlarm(Long memberNo) {

        List<Alarm> alarmList = alarmRepository.findAllByMember_MemberNo(memberNo);
        return alarmList.stream()
                .filter(alarm -> !alarm.isRead())
                .map(GetAllAlarmResponse::ofAlarmEntity)
                .toList();
    }

    public void createAlarm(CreateAlarmRequest request) throws FirebaseMessagingException {

        Member member = memberRepository.findByMemberNo(request.memberNo());
        Alarm alarm = Alarm.builder()
                .member(member)
                .title(request.title())
                .alarmType(request.alarmType())
                .targetPage(request.targetPage())
                .targetId(request.targetId())
                .build();

        alarmRepository.save(alarm);

        sendAlarm(request, member.getFcmToken());
    }

    private void sendAlarm(CreateAlarmRequest request, String fcmToken) throws FirebaseMessagingException {

        Notification notification = Notification.builder()
                        .setTitle(request.title())
                        .build();
        Message message = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(notification)
                        .build();

        firebaseMessaging.send(message);
    }

    @Transactional(readOnly = true)
    public void readAlarm(Long alarmNo) {

        Alarm alarm = alarmRepository.findById(alarmNo)
                .orElseThrow(EntityNotFoundException::new);

        alarm.readAlarm();
    }

}
