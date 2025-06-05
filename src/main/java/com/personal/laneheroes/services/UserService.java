package com.personal.laneheroes.services;

import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.response.ResponseWrapper;

import java.util.List;

public interface UserService {
    ResponseWrapper<User> addUser(User user);

    ResponseWrapper<User> updateUser(User user);

    ResponseWrapper<User> deleteUser(Long id);

    ResponseWrapper<List<User>> getAllUsers();

    ResponseWrapper<User> getUserById(Long id);
}
