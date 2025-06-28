package org.example.controller;

import org.example.kafka.UserEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final RestTemplate restTemplate;

    public NotificationController() {
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody UserEvent event) {
        try {
            String notificationUrl = "http://localhost:8081/api/test/send-email";
            String response = restTemplate.postForObject(notificationUrl, event, String.class);
            return ResponseEntity.ok("Уведомление отправлено: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка отправки уведомления: " + e.getMessage());
        }
    }
} 