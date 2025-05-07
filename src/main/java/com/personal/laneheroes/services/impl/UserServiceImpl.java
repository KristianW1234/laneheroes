package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.UserService;
import com.personal.laneheroes.utilities.PasswordUtil;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("UserServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;


    
    @Override
    public ResponseWrapper<User> addOrUpdateUser(User user, Boolean isUpdate) {
        User dbUser = new User();
        String successMsg = ResponseMessages.ADD_SUCCESS;
        String failMsg = ResponseMessages.ADD_FAIL;
        if (isUpdate){
            successMsg = ResponseMessages.UPDATE_SUCCESS;
            failMsg = ResponseMessages.UPDATE_FAIL;
            Optional<User> userPresence = userRepository.findById(user.getId());
            if (!userPresence.isPresent()){
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }
            dbUser = userPresence.get();
        }

        try {
            if (!isUpdate || user.getUserName() != null){
                dbUser.setUserName(user.getUserName());
            } else {
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (!isUpdate || user.getUserPassword() != null){

                dbUser.setUserPassword(PasswordUtil.encode(user.getUserPassword()));
            } else {
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (!isUpdate || user.getUserRole() != null){
                dbUser.setUserRole(user.getUserRole());
            } else {
                return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (!isUpdate || user.getUserEmail() != null){
                dbUser.setUserEmail(user.getUserEmail());
            }


            if (isUpdate || user.getIsActive() != null){
                dbUser.setIsActive(user.getIsActive());
            }

            if (isUpdate){
                dbUser.setUpdatedAt(LocalDateTime.now());
            } else {
                dbUser.setCreatedAt(LocalDateTime.now());
            }

            userRepository.save(dbUser);
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + successMsg,
                    ResponseMessages.SUCCESS_STATUS, dbUser);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.USER_SINGLE + " "
                    + failMsg,
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
}
