package com.personal.laneheroes.controllers;

import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllUsers() {
        ResponseWrapper<List<User>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<User>> getOneUser(@PathVariable Long id) {
        ResponseWrapper<User> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<User>> addUser(@Valid @RequestBody User user) {
        ResponseWrapper<User> response = userService.addOrUpdateUser(user, false);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseWrapper<User>> updateUser(@RequestBody User user) {
        ResponseWrapper<User> response = userService.addOrUpdateUser(user, true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<User>> deleteUser(@PathVariable Long id) {
        ResponseWrapper<User> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }


}
