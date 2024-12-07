package com.finance.common.event;

import com.google.protobuf.GeneratedMessageV3;

public interface EventProducer {
    void postEvent(String topic, byte[] event);

    void postEvent(String topic, GeneratedMessageV3 event);
}
