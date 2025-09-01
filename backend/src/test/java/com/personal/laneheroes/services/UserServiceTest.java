package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UserDTO;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.utilities.ResponseMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
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
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void resetMocks() {
        reset(userRepository);
    }

    UserDTO setupUserDTO(){
        UserDTO user = new UserDTO();
        user.setUserName("Name");
        user.setUserPassword("Pass1243");
        user.setUserEmail("mail@mail.com");
        user.setUserRole("Admin");
        user.setId(1L);
        user.setIsActive(true);
        return user;
    }
    
    User setupUser(){
        User user = new User();
        return user;
    }

    /*
     *
     * ADD USER TESTS
     *
     * */

    @Test
    void addUser_test_1(){
        UserDTO userDto = setupUserDTO();
        User user = setupUser();
        when(userRepository.save(any(User.class))).thenReturn(user);
        ResponseWrapper<User> trial = userService.addUser(userDto);
        verify(userRepository).save(any(User.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_2(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName(null);
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_3(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName("");
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_4(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserPassword(null);
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_5(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserPassword("");
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_6(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserRole(null);
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_7(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserEmail(null);
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_8(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserEmail("");
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_9(){
        UserDTO userDto = setupUserDTO();
        userDto.setIsActive(null);
        User user = setupUser();
        when(userRepository.save(any(User.class))).thenReturn(user);
        ResponseWrapper<User> trial = userService.addUser(userDto);
        verify(userRepository).save(any(User.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addUser_test_10(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserRole("Hacker");
        ResponseWrapper<User> trial = userService.addUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * UPDATE USER TESTS
     *
     * */

    @Test
    void updateUser_test_1(){
        UserDTO userDto = setupUserDTO();
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateUser_test_2(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName(null);
        userDto.setUserPassword(null);
        userDto.setUserEmail(null);
        userDto.setUserRole(null);
        userDto.setIsActive(null);
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateUser_test_3(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName("");
        userDto.setUserPassword("");
        userDto.setUserEmail("");
        userDto.setUserRole("");
        userDto.setIsActive(null);
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateUser_test_4(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName(null);
        userDto.setUserEmail(null);
        userDto.setUserRole(null);
        userDto.setIsActive(null);
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateUser_test_5(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName(null);
        userDto.setUserPassword(null);
        userDto.setUserRole(null);
        userDto.setIsActive(null);
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateUser_test_6(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName(null);
        userDto.setUserPassword(null);
        userDto.setUserEmail(null);
        userDto.setIsActive(null);
        userDto.setUserRole("Hacker");
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateUser_test_7(){
        UserDTO userDto = setupUserDTO();
        userDto.setUserName(null);
        userDto.setUserPassword(null);
        userDto.setUserEmail(null);
        userDto.setUserRole(null);
        userDto.setIsActive(false);
        ResponseWrapper<User> trial = userService.updateUser(userDto);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * DELETE USER TESTS
     *
     * */

    @Test
    void deleteUser_test_1(){
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        ResponseWrapper<User> trial = userService.deleteUser(any());
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void deleteUser_test_2(){
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<User> trial = userService.deleteUser(any());
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * GET ALL USERS TESTS
     *
     * */

    @Test
    void getAllUsers_test_1(){
        User user = setupUser();
        User user2 = setupUser();

        List<User> users = new ArrayList<>();

        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        ResponseWrapper<List<User>> trial = userService.getAllUsers();
        verify(userRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllUsers_test_2(){
        User user = setupUser();

        List<User> users = new ArrayList<>();

        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        ResponseWrapper<List<User>> trial = userService.getAllUsers();
        verify(userRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllUsers_test_3(){

        List<User> users = new ArrayList<>();


        when(userRepository.findAll()).thenReturn(users);

        ResponseWrapper<List<User>> trial = userService.getAllUsers();
        verify(userRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    /*
     *
     * GET USER BY ID TESTS
     *
     * */

    @Test
    void getUserById_test_1(){
        User user = setupUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        ResponseWrapper<User> trial = userService.getUserById(any());
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getUserById_test_2(){
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<User> trial = userService.getUserById(any());
        verify(userRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * SEARCH USERS TESTS
     *
     * */

    @Test
    void searchUsers_test_1(){
        User user = setupUser();
        User user2 = setupUser();

        List<User> users = new ArrayList();
        users.add(user);
        users.add(user2);

        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0,10), 1);

        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(userPage);

        ResponseWrapper<PagedResponse<User>> trial
                = userService.searchUsers("user-name", null, PageRequest.of(0, 10));

        verify(userRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchUsers_test_2(){
        User user = setupUser();


        List<User> users = new ArrayList();
        users.add(user);

        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0,10), 1);

        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(userPage);

        ResponseWrapper<PagedResponse<User>> trial
                = userService.searchUsers("user-name", null, PageRequest.of(0, 10));

        verify(userRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchUsers_test_3(){

        List<User> users = new ArrayList();

        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0,10), 0);

        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(userPage);

        ResponseWrapper<PagedResponse<User>> trial
                = userService.searchUsers("user-name", null,  PageRequest.of(0, 10));

        verify(userRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }


}
