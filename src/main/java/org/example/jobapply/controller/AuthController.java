package org.example.jobapply.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobapply.dto.AuthRequest;
import org.example.jobapply.dto.AuthResponse;
import org.example.jobapply.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest user){
        AuthResponse response = userService.login(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request){
        AuthResponse response =  userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
