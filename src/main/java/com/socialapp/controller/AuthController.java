package com.socialapp.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.socialapp.dto.AuthResponse;
import com.socialapp.dto.LoginReq;
import com.socialapp.dto.SignupReq;
import com.socialapp.model.entity.User;
import com.socialapp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth){
        this.auth = auth;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid SignupReq req) {
        return ResponseEntity.ok(auth.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginReq req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Access-Token") String token) {
        auth.logout(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(@RequestHeader("X-Access-Token") String token) {
        return ResponseEntity.ok(auth.me(token));
    }
}
