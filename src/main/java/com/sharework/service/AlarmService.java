package com.sharework.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sharework.common.AlarmTypeEnum;
import com.sharework.dao.UserAlarmDao;
import com.sharework.dao.UserDao;
import com.sharework.global.NotFoundException;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.Job;
import com.sharework.model.User;
import com.sharework.model.UserAlarm;
import com.sharework.request.model.AlarmRequest;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.meta.BasicMeta;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final TokenIdentification identification;
    private final UserDao userDao;
    private final UserAlarmDao userAlarmDao;

    public SuccessResponse register(String fcmToken, String accessToken) {
        long userId = identification.getHeadertoken(accessToken);
        User user = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow(() -> new NotFoundException("유저가 존재하지않습니다."));
        Optional<UserAlarm> userAlarm = userAlarmDao.findByUserId(user.getId());

        if (userAlarm.isPresent()) {
            userAlarm.get().setFcmToken(fcmToken);
            userAlarm.get().setUpdatedAt(LocalDateTime.now());
            userAlarmDao.save(userAlarm.get());
            return new SuccessResponse(new BasicMeta(true, "알림 유저 갱신 성공 입니다."));
        } else {
            userAlarmDao.save(UserAlarm.builder().fcmToken(fcmToken).userId(userId).build());
            return new SuccessResponse(new BasicMeta(true, "알림 유저 등록 성공 입니다."));
        }
    }

    public SuccessResponse sendAlarm(AlarmRequest request) {
        Message message = Message.builder()
            .setNotification(Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build())
            .setToken(request.getTargetFCMToken())
            .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("sendAlarm response: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("sendAlarm exception: " + e);
            return new SuccessResponse(new BasicMeta(false, "알림 전송 실패 입니다."));
        }

        return new SuccessResponse(new BasicMeta(true, "알림 전송 성공 입니다."));
    }

    public boolean sendAlarmType(AlarmTypeEnum alarmType, User worker, Job job) {
        String title = "";
        Optional<UserAlarm> targetUserAlarm = Optional.empty();

        switch (alarmType) {
            case JOB_APPLICATION_RECEIVED:
                title = String.format(alarmType.getTitle(), worker.getName());
                targetUserAlarm = userAlarmDao.findByUserId(job.getUserId());
                break;
            case SELECTED:
                title = String.format(alarmType.getTitle(), job.getTitle());
                targetUserAlarm = userAlarmDao.findByUserId(worker.getId());
                break;
            case DESELECTED:
            case JOB_START_REQUESTED:
            case JOB_RECRUIT_CLOSED:
            case JOB_FINISHED:
            case JOB_DONE:
                // TODO: to be implemented
                break;
        }

        if (targetUserAlarm.isEmpty()) {
            log.error("sendAlarmType empty, don't send alarm: {} {} {}", alarmType.name(), worker.getId(), job.getId());
            return false;
        }

        String targetFCMToken = targetUserAlarm.get().getFcmToken();

        Message message = Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(alarmType.getMessage())
                .build())
            .setToken(targetFCMToken)
            .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("sendAlarmType response: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("sendAlarmType exception: " + e);
            return false;
        }

        return true;
    }
}
