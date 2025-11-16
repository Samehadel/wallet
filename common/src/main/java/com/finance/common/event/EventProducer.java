package com.finance.common.event;


import com.finance.common.event.schema.user.Event;

public interface EventProducer {
    void postEvent(String topic, Event event);
}
