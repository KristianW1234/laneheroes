package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.LoginRequest;
import com.personal.laneheroes.dto.LoginResponse;
import com.personal.laneheroes.dto.LoginTokenDTO;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.LoginService;
import com.personal.laneheroes.utilities.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/laneHeroes/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /*@PostMapping("/login")
    public ResponseEntity<ResponseWrapper<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        ResponseWrapper<LoginResponse> response =  loginService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }*/

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<LoginTokenDTO>> loginJwt(@RequestBody @Valid LoginRequest loginRequest) {

                // Step 2: Generate JWT
        String jwtToken = jwtUtil.generateToken(loginRequest.getUserName());

        // Step 3: Use your existing loginService to get user data
        ResponseWrapper<LoginResponse> lr = loginService.authenticate(loginRequest);
        LoginResponse loginResponse = lr.getData();

        // Step 4: Combine LoginResponse + JWT into a custom wrapper
        LoginTokenDTO tokenResponse = new LoginTokenDTO(loginResponse, jwtToken);

        ResponseWrapper<LoginTokenDTO> response = new ResponseWrapper<>(
                "Login successful",
                lr.getStatus(),
                tokenResponse
        );

        return ResponseEntity.ok(response);
    }
}
