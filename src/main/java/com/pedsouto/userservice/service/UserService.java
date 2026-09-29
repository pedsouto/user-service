package com.pedsouto.userservice.service;

import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.entity.User;
import com.pedsouto.userservice.infra.repository.UserRepository;
import com.pedsouto.userservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
