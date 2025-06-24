package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.LoginRequest;
import com.personal.laneheroes.dto.LoginResponse;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.LoginService;
import com.personal.laneheroes.utilities.PasswordUtil;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;


    @Override
    public ResponseWrapper<LoginResponse> authenticate(LoginRequest request) {
        Optional<User> dbUser = userRepository.findByUserName(request.getUserName());

        if (dbUser.isEmpty()) {
            return new ResponseWrapper<>("User not found!",
                    ResponseMessages.FAIL_STATUS, null);
        }

        User user = dbUser.get();

        if (!PasswordUtil.matches(request.getUserPassword(), user.getUserPassword())) {
            return new ResponseWrapper<>("Invalid Password!",
                    ResponseMessages.FAIL_STATUS, null);
        }

        LoginResponse response = new LoginResponse(user.getId(), user.getUserName(), user.getUserRole().toString(), user.getIsActive());

        return new ResponseWrapper<>("Login successful", ResponseMessages.SUCCESS_STATUS, response);
    }
}
