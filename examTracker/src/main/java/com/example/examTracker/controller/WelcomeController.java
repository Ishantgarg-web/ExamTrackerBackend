package com.example.examTracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is for checking the application is working fine or not
 * with public endpoint.
 */

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @GetMapping("/checkHealth")
    public String checkHealth() {
        return "Application is working fine!! You are good to go";
    }

}
