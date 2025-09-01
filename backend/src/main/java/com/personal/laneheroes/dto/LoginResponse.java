package com.personal.laneheroes.dto;


public class LoginResponse {
    private Long userId;
    private String userName;
    private String role;
    private boolean active;

    public LoginResponse(Long userId, String userName, String role, boolean active) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
