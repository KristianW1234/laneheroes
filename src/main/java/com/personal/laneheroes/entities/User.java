package com.personal.laneheroes.entities;

import com.personal.laneheroes.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="app_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
