package com.example.examTracker.controller;

import com.example.examTracker.entity.AppUser;
import com.example.examTracker.enums.USERS_ROLE;
import com.example.examTracker.exceptions.OAuthException;
import com.example.examTracker.service.JwtUtil;
import com.example.examTracker.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {

    @Value("${spring.google.oauth.client_id}")
    private String client_id;

    @Value("${spring.google.oauth.client_secret}")
    private String client_secret;

    @Value("${spring.google.oauth.redirect_uri}")
    private String google_oauth_redirect_uri;

    @Value("${spring.google.oauth.response_cookie}")
    private boolean google_oauth_response_cookie;

    @Value("${spring.domain_name}")
    private String domain_name;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    private static Logger logger = LoggerFactory.getLogger(GoogleAuthController.class);

    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code, HttpServletResponse httpResponse) {
        try {
            // 1. Exchange authz code with tokens
            String tokenEndpoint = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", client_id);
            params.add("client_secret", client_secret);
//            params.add("redirect_uri", domain_name+"/auth/google/callback");
            params.add("redirect_uri", google_oauth_redirect_uri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType. APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");

            // 2. Get user info from token endpoint
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token="+idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            // 3. validate userInfoResponse
            if(userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                logger.info("userinfo is {}", userInfo);
                // Check user present in database or not with given email
                UserDetails userDetails = userService.loadUserByUsername(email);
                logger.info("Google auth userDetails {}", userDetails);
                if (userDetails == null) {
                    // it means user is not present in database
                    // create user in database
                    AppUser user = new AppUser();
                    user.setUsername((String) userInfo.get("name"));
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRole(USERS_ROLE.USER);
                    user.setTimeZone(ZoneId.systemDefault().toString());
                    AppUser appUser = userService.saveUser(user);
                }
                userDetails = userService.loadUserByUsername(email);
                // send jwt token in response to FE
                String jwt = jwtUtil.generateToken(userDetails);

                boolean isProd = google_oauth_response_cookie; // or read from profile/env

                ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                        .httpOnly(true)
                        .secure(isProd)                 // ❗ false on localhost
                        .sameSite(isProd ? "None" : "Lax")
                        .path("/")
                        .maxAge(Duration.ofDays(300))
                        .build();

                httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                httpResponse.sendRedirect(domain_name+"/onboarding");
                logger.info("Google oauth controller reached end...");
                return null;
//                return ResponseEntity.ok("Login successful for user "+ email);
            }
            throw new OAuthException("Google OAuth authentication failed");
        } catch (com.example.examTracker.exceptions.ApiException e) {
            // Re-throw API exceptions to be handled by global handler
            throw e;
        } catch (Exception e) {
            logger.error("Error in Google Auth Controller: {}", e.getMessage(), e);
            throw new OAuthException("Google OAuth authentication error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
