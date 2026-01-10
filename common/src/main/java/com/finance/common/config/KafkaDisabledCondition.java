package com.finance.common.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class KafkaDisabledCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String broker = context.getEnvironment().getProperty("event.message.broker");
        return broker != null && !broker.equalsIgnoreCase("kafka");
    }
}
