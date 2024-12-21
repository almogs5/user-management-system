package com.tasks.user_management_system.service;

import com.tasks.user_management_system.dto.LoginRequest;
import com.tasks.user_management_system.dto.User;
import com.tasks.user_management_system.exception.UserNotFoundException;
import com.tasks.user_management_system.service.db.UserDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tasks.user_management_system.util.ConstantsUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDbService userDbService;
    private final PasswordEncoder passwordEncoder;

    public User login(LoginRequest loginRequest) {
        User user = userDbService.getUserByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new UserNotFoundException(EMAIL_NOT_FOUND_ERROR + ": " + loginRequest.getEmail());
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(INVALID_CREDENTIALS_ERROR);
        }

        return user;
    }

    @Transactional
    public User signUp(User newUser) {
        User user = userDbService.getUserByEmail(newUser.getEmail());
        if (user != null) {
            throw new IllegalArgumentException(EMAIL_EXISTS_ERROR);
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setStatus(ACTIVE_STATUS);
        userDbService.saveUser(newUser);

        return newUser;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDbService.getAllUsers();
    }

    @Transactional
    public User updateStatus(String email, String status) {
        return userDbService.updateUserStatusByEmail(email, status);
    }
}
