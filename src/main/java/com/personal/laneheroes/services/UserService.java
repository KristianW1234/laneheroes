package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Role;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    ResponseWrapper<User> addUser(User user);

    ResponseWrapper<User> updateUser(User user);

    ResponseWrapper<User> deleteUser(Long id);

    ResponseWrapper<List<User>> getAllUsers();

    ResponseWrapper<User> getUserById(Long id);

    ResponseWrapper<PagedResponse<User>> searchUsers(String name, Role role, Pageable pageable);
}
