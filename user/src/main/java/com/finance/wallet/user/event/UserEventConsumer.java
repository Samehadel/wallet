package com.finance.wallet.user.event;

import com.finance.common.constants.TopicsNames;
import com.finance.common.event.schema.user.EventTypes;
import com.finance.common.event.schema.user.UserStatusMessage;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.wallet.user.service.UserStateService;
import com.google.protobuf.InvalidProtocolBufferException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserEventConsumer {
    private final UserStateService userStateService;
    private final ExceptionService exceptionService;

    @KafkaListener(topics = TopicsNames.USER_STATUS_UPDATE_TOPIC)
    public void consumeUserEvent(final byte[] message) {
        try {
            UserStatusMessage userStatusMessage = UserStatusMessage.parseFrom(message);

            if (userStatusMessage.getEventType() == EventTypes.FAILED_LOGIN_ATTEMPT) {
                userStateService.incrementFailedLoginTrials(userStatusMessage.getUserId());
            } else if (userStatusMessage.getEventType() == EventTypes.SUCCESSFUL_LOGIN) {
                userStateService.updateLastLoginDate(userStatusMessage.getUserId());
            }

        } catch (InvalidProtocolBufferException e) {
            throw exceptionService.buildBadExceptionWithReference(SharedApplicationError.GENERIC_ERROR);
        }
    }
}
