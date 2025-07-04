package com.personal.laneheroes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String userName;

    private String userPassword;

    private String userRole;

    private String userEmail;

    private Boolean isActive;
}
