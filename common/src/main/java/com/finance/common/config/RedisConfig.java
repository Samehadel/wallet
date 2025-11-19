package com.finance.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import java.io.IOException;

/**
 * Redis config.
 *
 * @author Sameh.Adel
 * @since 25/11/2024
 */
@Configuration
@Log4j2
@RequiredArgsConstructor
public class RedisConfig {
    private final ObjectMapper objectMapper;

    @Bean(name = "redissonClient", destroyMethod="shutdown")
    @ConditionalOnProperty(name = "application.cache.enabled", havingValue = "true")
    @Profile("dev")
    public RedissonClient redissonDev(@Value("classpath:/redisson-dev.yaml") Resource devConfigFile) throws IOException {
        final var config = Config.fromYAML(devConfigFile.getInputStream());
        config.setCodec(new JsonJacksonCodec(objectMapper));
        RedissonClient redissonClient = Redisson.create(config);
        log.info("Redisson Client configured for dev profile");

        return redissonClient;
    }

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    @ConditionalOnProperty(name = "application.cache.enabled", havingValue = "true")
    @Profile({"uat", "prod"})
    public RedissonClient redissonProd(@Value("classpath:/redisson-prod.yaml") Resource productionConfigFile) throws IOException {
        final var config = Config.fromYAML(productionConfigFile.getInputStream());
        config.setCodec(new JsonJacksonCodec(objectMapper));
        RedissonClient redissonClient = Redisson.create(config);
        log.info("Redisson Client configured for uat/prod profile");

        return redissonClient;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.byteArray());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.byteArray());
        template.afterPropertiesSet();

        return template;
    }
}
