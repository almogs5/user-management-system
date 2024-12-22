package com.tasks.user_management_system.service;

import com.tasks.user_management_system.dto.LoginRequest;
import com.tasks.user_management_system.dto.User;
import com.tasks.user_management_system.exception.UserNotFoundException;
import com.tasks.user_management_system.service.db.UserDbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDbService userDbService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void loginHappyFlow() {
        User user = initUser();

        when(userDbService.getUserByEmail(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        assertDoesNotThrow(() -> userService.login(new LoginRequest()));
    }

    @Test
    void whenUserNotFound_throwException() {
        when(userDbService.getUserByEmail(any())).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.login(new LoginRequest()));
    }

    @Test
    void whenPasswordInvalid_throwException() {
        when(userDbService.getUserByEmail(any())).thenReturn(initUser());
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.login(new LoginRequest()));
    }

    @Test
    void signUpHappyFlow() {
        User newUser = initUser();

        when(userDbService.getUserByEmail(any())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        User expectedUser = initUser();
        expectedUser.setPassword("encodedPassword");

        assertEquals(expectedUser, userService.signUp(newUser));
    }

    @Test
    void whenEmailExists_throwException() {
        when(userDbService.getUserByEmail(any())).thenReturn(initUser());
        assertThrows(IllegalArgumentException.class, () -> userService.signUp(initUser()));
    }

    @Test
    void updateStatusHappyFlow() {
        User expectedUser = initUser();
        expectedUser.setStatus("INACTIVE");

        when(userDbService.updateUserStatusByEmail(any(), any())).thenReturn(expectedUser);

        assertEquals(expectedUser, userService.updateStatus("asd123@asd.com", "INACTIVE"));
    }

    private User initUser() {
        return User.builder()
                .email("test@test.com")
                .status("ACTIVE")
                .password("password123")
                .build();
    }
}