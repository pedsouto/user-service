package com.pedsouto.userservice.service;

import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.dto.UserRegistrationDto;
import com.pedsouto.userservice.infra.entity.User;
import com.pedsouto.userservice.infra.repository.UserRepository;
import com.pedsouto.userservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto saveUser(UserRegistrationDto dto) {
        String email = normalizeEmail(dto.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado.");
        }
        User user = userMapper.toEntity(dto);
        user.setName(dto.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    public Long findUserIdByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email))
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"))
                .getId();
    }

    public UserDto findByEmail(String email, String authenticatedUserId) {
        User user = findAuthenticatedUser(authenticatedUserId);
        if (!user.getEmail().equalsIgnoreCase(normalizeEmail(email))) {
            throw new org.springframework.security.access.AccessDeniedException("Você só pode consultar seus próprios dados.");
        }
        return userMapper.toDto(user);
    }

    private User findAuthenticatedUser(String userId) {
        try {
            return userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        } catch (NumberFormatException ex) {
            throw new org.springframework.security.access.AccessDeniedException("Identificação inválida.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
