package com.finance.common.event;

import com.finance.common.event.schema.user.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "event.message.broker", havingValue = "redis")
@RequiredArgsConstructor
@Log4j2
public class RedisEventProducer implements EventProducer {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void postEvent(String topic, Event event) {
        ObjectRecord<String, Event> record = StreamRecords.newRecord()
                .ofObject(event)
                .withStreamKey(topic);

        redisTemplate.opsForStream().add(record);
    }
}
