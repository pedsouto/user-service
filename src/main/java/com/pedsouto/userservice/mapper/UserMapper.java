package com.pedsouto.userservice.mapper;

import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.dto.UserRegistrationDto;
import com.pedsouto.userservice.infra.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRegistrationDto dto);

    UserDto toDto(User entity);
}
