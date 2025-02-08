package com.example.controller;

import com.example.model.Notification;
import com.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Notification notification) {
        service.sendMessage(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk/{repeat}")
    public ResponseEntity<Void> sendMessage(@PathVariable Integer repeat, @RequestBody String message) {
        service.sendBulkMessages(repeat, message);
        return ResponseEntity.ok().build();
    }
}
