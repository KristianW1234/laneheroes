package com.personal.laneheroes.dto;


public class LoginTokenDTO {
    private LoginResponse user;
    private String token;

    public LoginTokenDTO(LoginResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public LoginResponse getUser() {
        return user;
    }

    public void setUser(LoginResponse user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
