package com.raihan.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/info")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(Map.of(
                "status", "online",
                "message", "Backend is running smoothly!",
                "version", "1.0.0"
        ));
    }
}