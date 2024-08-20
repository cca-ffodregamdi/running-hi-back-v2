package com.runninghi.runninghibackv2.application.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.runninghi.runninghibackv2.application.dto.alarm.request.CreateAlarmRequest;
import com.runninghi.runninghibackv2.application.dto.alarm.response.GetAllAlarmResponse;
import com.runninghi.runninghibackv2.domain.entity.Alarm;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.repository.AlarmRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final FirebaseMessaging firebaseMessaging;

    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public List<GetAllAlarmResponse> getAllPushAlarms(Long memberNo) {

        // 알람이 없으면 어차피 리스트는 텅! 이므로 알림동의 여부 파악 x
        List<Alarm> alarmList = alarmRepository.findAllByMember_MemberNo(memberNo);
        List<GetAllAlarmResponse> alarmResponseList = alarmList.stream()
                .filter(alarm -> !alarm.isRead())
                .map(GetAllAlarmResponse::ofAlarmEntity)
                .toList();

        for (Alarm alarm : alarmList) {
            alarm.readAlarm();
        }

        return alarmResponseList;
    }

    private final MemberRepository memberRepository;

    public void createPushAlarm(CreateAlarmRequest request) throws FirebaseMessagingException {

        Member member = memberRepository.findByMemberNo(request.getTargetMemberNo());
        Alarm alarm = Alarm.builder()
                .member(member)
                .title(request.getTitle())
                .alarmType(request.getAlarmType())
                .targetPage(request.getTargetPage())
                .targetId(request.getTargetId())
                .build();

        alarmRepository.save(alarm);

        if (member.isAlarmConsent()) { // 알림 동의했을 시 발송, 아닐 시 stop
            sendPushAlarm(request);
        }
    }

    private void sendPushAlarm (CreateAlarmRequest request) throws FirebaseMessagingException {

        Notification notification = Notification.builder()
                        .setTitle(request.getTitle())
                        .build();
        Message message = Message.builder()
                        .setToken(request.getFcmToken())
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

    @Transactional
    public void deleteAlarm(Long alarmNo) {
        alarmRepository.deleteById(alarmNo);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanUpReadAlarm() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        alarmRepository.deleteReadAlarmsOlderThan(thirtyDaysAgo);
    }



}
