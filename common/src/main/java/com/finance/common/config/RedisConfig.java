package com.finance.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import lombok.RequiredArgsConstructor;

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

    @Bean(destroyMethod="shutdown")
    @Profile("dev")
    public RedissonClient redissonDev(@Value("classpath:/redisson-dev.yaml") Resource devConfigFile) throws IOException {
        final var config = Config.fromYAML(devConfigFile.getInputStream());
        return Redisson.create(config);
    }


    @Bean(destroyMethod = "shutdown")
    @Profile({"prod", "sit"})
    public RedissonClient redissonProd(@Value("classpath:/redisson-prod.yaml") Resource productionConfigFile) throws IOException {
        final var config = Config.fromYAML(productionConfigFile.getInputStream());
        return Redisson.create(config);
    }

}
