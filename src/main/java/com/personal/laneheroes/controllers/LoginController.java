package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.LoginRequest;
import com.personal.laneheroes.dto.LoginResponse;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/laneheroes/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        ResponseWrapper<LoginResponse> response =  loginService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
}
