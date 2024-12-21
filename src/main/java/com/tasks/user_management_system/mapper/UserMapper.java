package com.tasks.user_management_system.mapper;

import com.tasks.user_management_system.dto.User;
import com.tasks.user_management_system.repository.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserDto(UserEntity userEntity);

    UserEntity toUserEntity(User newUser);
}
