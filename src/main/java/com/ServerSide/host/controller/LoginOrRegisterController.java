/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.controller;

import com.ServerSide.host.dto.LoginForm;
import com.ServerSide.host.dto.LoginResponse;
import com.ServerSide.host.dto.RegisterForm;
import com.ServerSide.host.service.LoginOrRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginOrRegisterController {

    private final LoginOrRegisterService userService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<LoginResponse> register(@ModelAttribute RegisterForm registerForm) {
        LoginResponse response = userService.register(registerForm);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginForm loginForm) {
        LoginResponse response = userService.login(loginForm);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "✅ Ini endpoint publik, tidak perlu token.";
    }
}
