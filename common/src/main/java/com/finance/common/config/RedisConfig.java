package com.finance.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

import io.lettuce.core.ClientOptions;

/**
 * Redis config.
 *
 * @author Sameh.Adel
 * @since 25/11/2024
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "enable.redis.cache", havingValue = "true")
public class RedisConfig {
    private final ObjectMapper objectMapper;

    @Bean("lettuceConnectionFactory")
    @Profile("dev")
    public LettuceConnectionFactory lettuceConnectionFactoryDev(final RedisProperties redisProperties) {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean("lettuceConnectionFactory")
    @Profile({"prod", "sit"})
    public LettuceConnectionFactory lettuceConnectionFactory(final RedisProperties redisProperties) {
        var redisSentinelConfiguration = new RedisSentinelConfiguration();
        redisSentinelConfiguration.master(redisProperties.getSentinel().getMaster());
        redisSentinelConfiguration.setSentinels(redisProperties.getSentinel()
            .getNodes()
            .stream()
            .map(RedisNode::fromString)
            .toList());

        var clientOptions = ClientOptions.builder()
            .pingBeforeActivateConnection(true)
            .autoReconnect(true)
            .build();

        var lettuceClientConfiguration = LettuceClientConfiguration.builder()
            .clientOptions(clientOptions)
            .build();

        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }

    @Bean
    @ConditionalOnBean(LettuceConnectionFactory.class)
    @DependsOn("lettuceConnectionFactory")
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.afterPropertiesSet();

        return template;
    }
}
