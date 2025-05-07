package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.LoginRequest;
import com.personal.laneheroes.dto.LoginResponse;
import com.personal.laneheroes.response.ResponseWrapper;

public interface LoginService {
    public ResponseWrapper<LoginResponse> authenticate(LoginRequest request);
}
