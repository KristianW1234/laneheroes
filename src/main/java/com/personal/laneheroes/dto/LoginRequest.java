package com.personal.laneheroes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username must not be blank")
    private String userName;

    @NotBlank(message = "Password must not be blank")
    private String userPassword;
}
