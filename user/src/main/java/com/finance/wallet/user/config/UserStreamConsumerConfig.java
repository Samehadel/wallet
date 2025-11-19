package com.finance.wallet.user.config;

import com.finance.common.constants.TopicsNames;
import com.finance.common.event.RedisStreamConsumerConfig;
import com.finance.wallet.user.event.RedisUserEventConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

@Configuration
@ConditionalOnProperty(value = "event.message.broker", havingValue = "redis")
@RequiredArgsConstructor
public class UserStreamConsumerConfig extends RedisStreamConsumerConfig {
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.application.name:user-service}")
    private String applicationName;

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, byte[]>> userEventListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisUserEventConsumer listener) {

        return createContainer(
                connectionFactory,
                redisTemplate,
                listener,
                TopicsNames.USER_STATUS_UPDATE_TOPIC,               // Stream name
                "user-service-group",                               // Consumer group
                applicationName + "-1"
        );
    }
}
