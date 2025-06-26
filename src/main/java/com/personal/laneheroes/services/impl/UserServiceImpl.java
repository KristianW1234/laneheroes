package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Role;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.UserService;
import com.personal.laneheroes.specifications.HeroSpecification;
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
    public ResponseWrapper<User> addUser(User user) {
        User dbUser = new User();

        try {
            dbUser.setUserName(user.getUserName());
            dbUser.setUserPassword(PasswordUtil.encode(user.getUserPassword()));
            dbUser.setUserRole(user.getUserRole());
            dbUser.setUserEmail(user.getUserEmail());
            dbUser.setIsActive(user.getIsActive());
            dbUser.setCreatedAt(LocalDateTime.now());
            userRepository.save(dbUser);
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.ADD_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbUser);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.ADD_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        
    }

    @Override
    public ResponseWrapper<User> updateUser(User user) {

        Optional<User> userPresence = userRepository.findById(user.getId());
        if (userPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        User dbUser = userPresence.get();

        try {
            if (user.getUserName() != null){
                dbUser.setUserName(user.getUserName());
            } else {
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + ResponseMessages.UPDATE_FAIL,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (user.getUserPassword() != null){

                dbUser.setUserPassword(PasswordUtil.encode(user.getUserPassword()));
            } else {
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + ResponseMessages.UPDATE_FAIL,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (user.getUserRole() != null){
                dbUser.setUserRole(user.getUserRole());
            } else {
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + ResponseMessages.UPDATE_FAIL,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (user.getUserEmail() != null){
                dbUser.setUserEmail(user.getUserEmail());
            }


            if (user.getIsActive() != null){
                dbUser.setIsActive(user.getIsActive());
            }

            dbUser.setUpdatedAt(LocalDateTime.now());

            userRepository.save(dbUser);
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.UPDATE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbUser);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

    }

    @Override
    public ResponseWrapper<User> deleteUser(Long id) {
        Optional<User> userPresence = userRepository.findById(id);
        if (userPresence.isPresent()){
            User dbUser = userPresence.get();
            userRepository.delete(dbUser);
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbUser);
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
}
