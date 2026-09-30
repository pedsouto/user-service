package com.pedsouto.userservice.service;

import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.entity.User;
import com.pedsouto.userservice.infra.exception.ConflictException;
import com.pedsouto.userservice.infra.exception.ResourceNotFoundException;
import com.pedsouto.userservice.infra.repository.UserRepository;
import com.pedsouto.userservice.infra.security.JwtUtil;
import com.pedsouto.userservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        validateEmail(userDto.getEmail());

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        userRepository.deleteByEmail(email);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already registered: " + email);
        }
    }

    @Transactional
    public UserDto updateUser(String token, UserDto userDto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        userDto.setPassword(userDto.getPassword() != null ? passwordEncoder.encode(userDto.getPassword()) : null);

        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        userMapper.updateUserFromDto(userDto, userEntity);

        return userMapper.toDto(userRepository.save(userEntity));
    }
}
