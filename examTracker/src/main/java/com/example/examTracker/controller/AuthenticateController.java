package com.example.examTracker.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthenticateController {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthenticateController.class);

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(
            HttpServletResponse response,
            Authentication authentication
    ) {
        if (authentication != null) {
            logger.info("Logging out user: {}", authentication.getName());
        } else {
            logger.info("Logout request without authentication");
        }

        boolean isProd = false; // read from profile later

        ResponseCookie deleteCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(isProd)
                .sameSite(isProd ? "None" : "Lax")
                .path("/")
                .maxAge(0)   // 🔥 delete cookie
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.noContent().build();
    }
}

