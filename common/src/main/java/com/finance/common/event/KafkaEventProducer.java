package com.finance.common.event;

import com.google.protobuf.GeneratedMessageV3;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaEventProducer implements EventProducer {
    private final KafkaTemplate<String, byte[]> template;

    @Override
    public void postEvent(final String topic, final byte[] event) {
        CompletableFuture<SendResult<String, byte[]>> future = this.template.send(topic, event).toCompletableFuture();
        future.whenComplete((result, ex) -> {
            if (Objects.isNull(ex)) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Message sent successfully to topic: {}, partition: {}, offset: {}", new Object[]{metadata.topic(), metadata.partition(), metadata.offset()});
            } else {
                log.error("Failed to send message to topic: {}, error: {}", topic, ex.getMessage());
            }
        });
    }

    @Override
    public void postEvent(final String topic, final GeneratedMessageV3 event) {
        postEvent(topic, event.toByteArray());
    }
}
