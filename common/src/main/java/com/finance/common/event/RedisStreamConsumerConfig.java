package com.finance.common.event;

import com.finance.common.event.schema.user.Event;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Log4j2
public abstract class RedisStreamConsumerConfig {

    protected StreamMessageListenerContainer<String, ObjectRecord<String, byte[]>> createContainer(
            RedisConnectionFactory connectionFactory,
            RedisTemplate<String, Object> redisTemplate,
            StreamListener<String, ObjectRecord<String, byte[]>> listener,
            String streamName,
            String consumerGroup,
            String consumerName) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, byte[]>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(1))
                        .targetType(byte[].class)
                        .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, byte[]>> container =
                StreamMessageListenerContainer.create(connectionFactory, options);

        createGroupIfNotExists(redisTemplate, streamName, consumerGroup);

        // Subscribe to stream with consumer group
        container.receive(
                Consumer.from(consumerGroup, consumerName),
                StreamOffset.create(streamName, ReadOffset.from("0")),
                listener
        );

        container.start();
        return container;
    }

    private void createGroupIfNotExists(RedisTemplate<String, Object> redisTemplate, String streamName, String consumerGroup) {
        try {
            redisTemplate.opsForStream().createGroup(streamName, ReadOffset.from("0"), consumerGroup);
        } catch (Exception ignored) {
            // group or stream already exists — totally fine
            log.info("Group or stream already exists");
        }
    }
}
