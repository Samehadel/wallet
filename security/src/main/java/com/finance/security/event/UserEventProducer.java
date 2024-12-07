package com.finance.security.event;


import com.finance.common.constants.TopicsNames;
import com.finance.common.event.EventProducer;
import com.finance.common.event.schema.user.EventTypes;
import com.finance.common.event.schema.user.UserStatus;
import com.finance.common.event.schema.user.UserStatusMessage;
import com.finance.common.util.StringUtil;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserEventProducer {
    private final EventProducer eventProducer;

    public void pushIncreaseFailedLoginAttemptsEvent(final Long userId) {
        log.info("Posting Increase Failed Login Attempts Event for userId:[{}]", userId);

        pushEvent(userId, null, EventTypes.STATUS_CHANGE, null);
    }

    public void pushBlockUserEvent(final Long userId, final String reason) {
        log.info("Posting Block User Event for userId:[{}]", userId);
        pushEvent(userId, UserStatus.BLOCKED, EventTypes.STATUS_CHANGE, reason);
    }

    public void pushLockUserEvent(final Long userId, final String reason) {
        log.info("Posting Lock User Event for userId:[{}]", userId);
        pushEvent(userId, UserStatus.LOCKED, EventTypes.STATUS_CHANGE, reason);
    }

    private void pushEvent(final Long userId, final UserStatus userStatus, final EventTypes eventType, final String reason) {
        UserStatusMessage userStatusMessage = UserStatusMessage.newBuilder()
            .setEventType(eventType)
            .setUserId(userId)
            .setNewStatusTime(System.currentTimeMillis())
            .build();

        if (userStatus != null) {
            userStatusMessage = userStatusMessage.toBuilder().setNewStatus(userStatus).build();
        }

        if (!StringUtil.isNullOrEmpty(reason)) {
            userStatusMessage = userStatusMessage.toBuilder().setReason(reason).build();
        }

        eventProducer.postEvent(TopicsNames.USER_STATUS_UPDATE_TOPIC, userStatusMessage);
    }
}
