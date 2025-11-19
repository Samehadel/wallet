package com.finance.common.event;

import com.finance.common.event.schema.user.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnProperty(value = "event.message.broker", havingValue = "kafka")
@RequiredArgsConstructor
@Log4j2
public class KafkaEventProducer implements EventProducer {
    private final KafkaTemplate<String, byte[]> template;

    @Override
    public void postEvent(final String topic, final Event event) {
        CompletableFuture<SendResult<String, byte[]>> future = this.template.send(topic, event.toByteArray()).toCompletableFuture();
        future.whenComplete((result, ex) -> {
            if (Objects.isNull(ex)) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message sent successfully to topic: {}, partition: {}, offset: {}", new Object[]{metadata.topic(), metadata.partition(), metadata.offset()});
            } else {
                log.error("Failed to send message to topic: {}, error: {}", topic, ex.getMessage());
            }
        });
    }
}
