package org.example.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserEvent event) {
        logger.info("Отправка сообщения: {}", event);
        kafkaTemplate.send("user-events", event);
    }
}