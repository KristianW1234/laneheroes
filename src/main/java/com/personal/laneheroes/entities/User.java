package com.personal.laneheroes.entities;

import com.personal.laneheroes.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="app_user")
public class User {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LH_USER_ID_S")
    @SequenceGenerator(name = "LH_USER_ID_S", allocationSize = 1, sequenceName = "LH_USER_ID_S")
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "USER_PASSWORD", nullable = false, length = 255)
    private String userPassword;

    @Column(name = "USER_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = false;

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name="LAST_LOGIN")
    private LocalDateTime lastLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
