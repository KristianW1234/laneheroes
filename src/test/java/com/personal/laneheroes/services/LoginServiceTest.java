package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.LoginRequest;
import com.personal.laneheroes.dto.LoginResponse;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Role;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.utilities.PasswordUtil;
import com.personal.laneheroes.utilities.ResponseMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
})
@Import(TestMockConfig.class)
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void resetMocks() {
        reset(userRepository);
    }

    LoginRequest setup(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("User");
        loginRequest.setUserPassword("Pass123");
        return loginRequest;
    }

    @Test
    void authenticate_test_1(){

        LoginRequest loginRequest = setup();
        User user = new User();
        user.setUserName("User");
        user.setUserPassword(PasswordUtil.encode("Pass123"));
        user.setUserEmail("User@user.com");
        user.setIsActive(true);
        user.setUserRole(Role.ADMIN);
        user.setId(1L);

        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        ResponseWrapper<LoginResponse> response = loginService.authenticate(loginRequest);
        verify(userRepository).findByUserName(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());


    }

    @Test
    void authenticate_test_2(){

        LoginRequest loginRequest = setup();
        when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        ResponseWrapper<LoginResponse> response = loginService.authenticate(loginRequest);
        verify(userRepository).findByUserName(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());


    }

    @Test
    void authenticate_test_3(){

        LoginRequest loginRequest = setup();
        User user = new User();
        user.setUserName("User");
        user.setUserPassword("Pass123");

        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        ResponseWrapper<LoginResponse> response = loginService.authenticate(loginRequest);
        verify(userRepository).findByUserName(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());


    }
}
