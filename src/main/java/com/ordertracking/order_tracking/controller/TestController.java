package com.ordertracking.order_tracking.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ordertracking.order_tracking.model.User;
import com.ordertracking.order_tracking.repository.UserRepository;

@RestController
public class TestController {

    @Autowired
    private UserRepository repo;

    @GetMapping("/test")
    public String test() {
        User u = User.builder()
                .name("Test User")
                .email("testuser@example.com")
                .role("Customer")
                .build();

        repo.save(u);
        return "✅ User saved successfully with ID: " + u.getUserId();
    }
}
