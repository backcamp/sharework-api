package com.sharework.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.sharework.dao.ApplicationDao;
import com.sharework.dao.JobDao;
import com.sharework.dao.UserAlarmDao;
import com.sharework.dao.UserDao;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.Job;
import com.sharework.model.ResidentNumberJsonb;
import com.sharework.model.User;
import com.sharework.model.UserAlarm;
import com.sharework.request.model.APIApplicationApplied;
import com.sharework.response.model.SuccessResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationDao applicationDao;
    @Mock
    private JobDao jobDao;
    @Mock
    private UserAlarmDao userAlarmDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TokenIdentification identification;
    @Mock
    private AlarmService alarmService;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void insertApplication() {
        // given
        when(identification.getHeadertoken(any())).thenReturn(123L);
        when(applicationDao.findByJobIdAndUserId(anyLong(), anyLong())).thenReturn(null);

        Job mockJob = new Job(
            1L,
            1L,
            "강남역이사도와주실한분구함",
            LocalDateTime.of(2023, 10, 18, 9, 0),
            LocalDateTime.of(2023, 10, 19, 18, 0),
            37.9,
            126.9,
            "서울강남구삼성동47-26",
            "우봉빌라201호",
            1L,
            "시급",
            10000,
            "개꿀임",
            "OPEN",
            true
        );
        when(jobDao.findById(any())).thenReturn(Optional.of(mockJob));

        UserAlarm mockUserAlarm = new UserAlarm(
            "TEST_FCM_TOKEN_123456789",
            1L
        );
        when(userAlarmDao.findByUserId(anyLong())).thenReturn(Optional.of(mockUserAlarm));

        User mockUser = new User(
            "sharework@sharework.com",
            "김쉐어",
            "01012345678",
            new ResidentNumberJsonb("1", "1"),
            "GIVER",
            "TEST_REFRESH_TOKEN_123456789",
            "password"
        );
        when(userDao.findById(anyLong())).thenReturn(Optional.of(mockUser));

        when(alarmService.sendAlarmType(any(), any(), any())).thenReturn(true);

        // when
        APIApplicationApplied application = new APIApplicationApplied(new ArrayList<>(1), 1L, 37.9, 126.9);
        SuccessResponse response = applicationService.insertApplication(application, any());

        // then
        Assertions.assertTrue(response.getMeta().isStatus());
        Assertions.assertEquals("", response.getMeta().getMessage());
    }
}
