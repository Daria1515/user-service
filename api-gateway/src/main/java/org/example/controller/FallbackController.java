package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public ResponseEntity<String> userServiceFallback() {
        return ResponseEntity.ok("User Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/notification-service")
    public ResponseEntity<String> notificationServiceFallback() {
        return ResponseEntity.ok("Notification Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/external-service")
    public ResponseEntity<String> externalServiceFallback() {
        return ResponseEntity.ok("External Service is currently unavailable. Please try again later.");
    }
} 