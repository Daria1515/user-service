package org.example.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class UserEventProducerTest {

    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    private UserEventProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producer = new UserEventProducer(kafkaTemplate);
    }

    @Test
    void testSendCreateEvent() {
        UserEvent event = new UserEvent("test@example.com", UserEvent.OPERATION_CREATE);
        
        producer.send(event);
        
        verify(kafkaTemplate).send("user-events", event);
    }

    @Test
    void testSendDeleteEvent() {
        UserEvent event = new UserEvent("test@example.com", UserEvent.OPERATION_DELETE);
        
        producer.send(event);
        
        verify(kafkaTemplate).send("user-events", event);
    }

    @Test
    void testSendUpdateEvent() {
        UserEvent event = new UserEvent("test@example.com", UserEvent.OPERATION_UPDATE);
        
        producer.send(event);
        
        verify(kafkaTemplate).send("user-events", event);
    }
} 