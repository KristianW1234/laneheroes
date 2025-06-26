package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Gender;
import com.personal.laneheroes.enums.Role;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.UserService;
import com.personal.laneheroes.utilities.Utility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
        ResponseWrapper<User> response = userService.addUser(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseWrapper<User>> updateUser(@RequestBody User user) {
        ResponseWrapper<User> response = userService.updateUser(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<User>> deleteUser(@PathVariable Long id) {
        ResponseWrapper<User> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PagedResponse<User>>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        ResponseWrapper<PagedResponse<User>> response = userService.searchUsers(name, role, Utility.setupPageable(page, size, sortBy, sortOrder));
        return ResponseEntity.ok(response);
    }


}
