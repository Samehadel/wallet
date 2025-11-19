package com.finance.wallet.user.event;

import com.finance.common.event.schema.user.Event;
import com.finance.common.event.schema.user.EventTypes;
import com.finance.common.event.schema.user.UserStatusMessage;
import com.finance.wallet.user.service.UserStateService;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUserEventConsumer implements StreamListener<String, ObjectRecord<String, byte[]>> {
    private final UserStateService userStateService;

    @Override
    public void onMessage(ObjectRecord<String, byte[]> message) {
        Event event = buildEventOrNullOnException(message);
        UserStatusMessage userStatusMessage = event.getUserStatusMessage();

        if (event.getEventType() == EventTypes.FAILED_LOGIN_ATTEMPT) {
            userStateService.incrementFailedLoginTrials(userStatusMessage.getUserId());
        } else if (event.getEventType() == EventTypes.SUCCESSFUL_LOGIN) {
            userStateService.updateLastLoginDate(userStatusMessage.getUserId());
        }
    }

    private Event buildEventOrNullOnException(ObjectRecord<String, byte[]> message) {
        Event event;
        try {
            event = Event.parseFrom(message.getValue());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        return event;
    }
}
