package com.oneamz.inventory.controller;

import com.oneamz.inventory.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Api(tags = "Authentication")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @ApiOperation(value = "Login", notes = "Authenticates a user and returns a JWT token")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if ("user".equals(username) && "password".equals(password)) {
            List<String> roles = Collections.singletonList("USER");
            String token = jwtTokenProvider.generateToken(username, roles);
            return ResponseEntity.ok(token);
        } else if ("admin".equals(username) && "admin".equals(password)) {
            List<String> roles = Collections.singletonList("ADMIN");
            String token = jwtTokenProvider.generateToken(username, roles);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
