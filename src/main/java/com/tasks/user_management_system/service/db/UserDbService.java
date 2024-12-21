package com.tasks.user_management_system.service.db;

import com.tasks.user_management_system.dto.User;
import com.tasks.user_management_system.exception.UserNotFoundException;
import com.tasks.user_management_system.mapper.UserMapper;
import com.tasks.user_management_system.repository.UserRepository;
import com.tasks.user_management_system.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.tasks.user_management_system.util.ConstantsUtil.USER_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class UserDbService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Cacheable(value = "users" , key = "userEmail")
    public User getUserByEmail(String userEmail) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(userEmail);
        return userEntity.map(userMapper::toUserDto).orElse(null);
    }

    @CacheEvict(value = "users" , key = "newUser.email")
    public User saveUser(User newUser) {
        UserEntity userEntity = userMapper.toUserEntity(newUser);
        return userMapper.toUserDto(userRepository.save(userEntity));
    }

    @CacheEvict(value = "users" , key = "userEmail")
    public User updateUserStatusByEmail(String userEmail, String status) {
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));

        userEntity.setStatus(status);
        return userMapper.toUserDto(userRepository.save(userEntity));
    }

    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }
}
