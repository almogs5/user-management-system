package com.tasks.user_management_system.controller;

import com.tasks.user_management_system.dto.LoginRequest;
import com.tasks.user_management_system.dto.StatisticsResponse;
import com.tasks.user_management_system.dto.User;
import com.tasks.user_management_system.service.StatisticsService;
import com.tasks.user_management_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{email}/status")
    public ResponseEntity<User> updateStatus(@PathVariable String email, @RequestParam String status) {
        User updatedUser = userService.updateStatus(email, status);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest loginRequest) {
        User loginedUser = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginedUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody User user) {
        User createdUser = userService.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/stats")
    public ResponseEntity<StatisticsResponse> getStats() {
        StatisticsResponse stats = statisticsService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
