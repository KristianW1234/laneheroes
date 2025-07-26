package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UserDTO;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Role;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.UserService;
import com.personal.laneheroes.specifications.UserSpecification;
import com.personal.laneheroes.utilities.PasswordUtil;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;


    
    @Override
    public ResponseWrapper<User> addUser(UserDTO user) {
        if (user.getUserName() == null || user.getUserName().isBlank() ||
                user.getUserPassword() == null || user.getUserPassword().isBlank() ||
                user.getUserRole() == null ||
                user.getUserEmail() == null || user.getUserEmail().isBlank()){
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.ADD_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

        User dbUser = new User();

        dbUser.setUserName(user.getUserName());
        dbUser.setUserPassword(PasswordUtil.encode(user.getUserPassword()));
        try {
            dbUser.setUserRole(Role.valueOf(user.getUserRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return new ResponseWrapper<>(
                    "Invalid role: " + user.getUserRole(),
                    ResponseMessages.FAIL_STATUS,
                    null
                );
            }

        dbUser.setUserEmail(user.getUserEmail());
        dbUser.setIsActive(Boolean.TRUE.equals(user.getIsActive()));
        dbUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(dbUser);
        return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                + ResponseMessages.ADD_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbUser);

    }

    @Override
    public ResponseWrapper<User> updateUser(UserDTO user) {

        if (hasNoUpdatableFields(user)) {
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.ADD_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

        Optional<User> userPresence = userRepository.findById(user.getId());
        if (userPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        User dbUser = userPresence.get();


            if (user.getUserName() != null){
                dbUser.setUserName(user.getUserName());
            }

            if (user.getUserPassword() != null && !user.getUserPassword().isBlank()){

                dbUser.setUserPassword(PasswordUtil.encode(user.getUserPassword()));
            }

            if (user.getUserEmail() != null){
                dbUser.setUserEmail(user.getUserEmail());
            }


            if (user.getIsActive() != null){
                dbUser.setIsActive(user.getIsActive());
            }

            if (user.getUserRole() != null) {
                try {
                    dbUser.setUserRole(parseRole(user.getUserRole()));
                } catch (IllegalArgumentException e) {
                    return new ResponseWrapper<>(
                            "Invalid role: " + user.getUserRole(),
                            ResponseMessages.FAIL_STATUS,
                            null
                    );
                }
            }



            dbUser.setUpdatedAt(LocalDateTime.now());

            userRepository.save(dbUser);
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.UPDATE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbUser);


    }

    @Override
    public ResponseWrapper<User> deleteUser(Long id) {
        Optional<User> userPresence = userRepository.findById(id);
        if (userPresence.isPresent()){
            User dbUser = userPresence.get();
            userRepository.delete(dbUser);
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, null);
        } else {
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }

    @Override
    public ResponseWrapper<List<User>> getAllUsers() {
        List<User> list = userRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.USER_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.USER_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<User> getUserById(Long id) {
        Optional<User> userPresence = userRepository.findById(id);
        return userPresence.map(
                        user -> new ResponseWrapper<>(ResponseMessages.USER_SINGLE
                                + " " + ResponseMessages.FOUND,
                                ResponseMessages.SUCCESS_STATUS, user))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.USER_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<PagedResponse<User>> searchUsers(String name, Role role, Pageable pageable) {
        Specification<User> spec = UserSpecification.withFilters(name, role);
        Page<User> resultPage = userRepository.findAll(spec, pageable);
        PagedResponse<User> pagedResponse = new PagedResponse<>(resultPage);

        if (resultPage.hasContent()) {
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + resultPage.getNumberOfElements() + " ";
            if (resultPage.getNumberOfElements() > 1) {
                successMessage += ResponseMessages.USER_PLURAL.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.USER_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.USER_SINGLE.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.USER_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        }
    }

    private boolean hasNoUpdatableFields(UserDTO user) {
        return (user.getUserName() == null || user.getUserName().isBlank()) &&
                (user.getUserPassword() == null || user.getUserPassword().isBlank()) &&
                (user.getUserRole() == null || user.getUserRole().isBlank()) &&
                (user.getUserEmail() == null || user.getUserEmail().isBlank()) &&
                user.getIsActive() == null;
    }

    private Role parseRole(String roleString) {
        try {
            return Role.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleString);
        }
    }
}
