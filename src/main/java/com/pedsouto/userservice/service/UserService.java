package com.pedsouto.userservice.service;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.dto.UserRegistrationDto;
import com.pedsouto.userservice.dto.UserUpdateDto;
import com.pedsouto.userservice.infra.entity.Address;
import com.pedsouto.userservice.infra.entity.Phone;
import com.pedsouto.userservice.infra.entity.User;
import com.pedsouto.userservice.infra.exception.ConflictException;
import com.pedsouto.userservice.infra.exception.ResourceNotFoundException;
import com.pedsouto.userservice.infra.repository.AddressRepository;
import com.pedsouto.userservice.infra.repository.PhoneRepository;
import com.pedsouto.userservice.infra.repository.UserRepository;
import com.pedsouto.userservice.mapper.UserMapper;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto saveUser(UserRegistrationDto dto) {
        String email = normalizeEmail(dto.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Este e-mail já está cadastrado.");
        }
        User user = userMapper.toEntity(dto);
        user.setName(dto.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.password()));
        if (dto.addresses() != null) {
            for (AddressDto addressDto : dto.addresses()) {
                user.addAddress(userMapper.toAddressEntity(addressDto));
            }
        }
        if (dto.phones() != null) {
            for (PhoneDto phoneDto : dto.phones()) {
                user.addPhone(userMapper.toPhoneEntity(phoneDto));
            }
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public PhoneDto addPhone(String userId, PhoneDto dto) {
        User user = findAuthenticatedUser(userId);
        Phone phone = userMapper.toPhoneEntity(dto);
        phone.setUser(user);
        return userMapper.toPhoneDto(phoneRepository.save(phone));
    }

    @Transactional
    public PhoneDto updatePhone(String userId, Long id, PhoneDto dto) {
        Long ownerId = Long.valueOf(userId);
        Phone phone = phoneRepository.findByIdAndUser_Id(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado."));
        userMapper.updatePhoneFromDto(dto, phone);
        return userMapper.toPhoneDto(phone);
    }

    @Transactional
    public AddressDto addAddress(String userId, AddressDto dto) {
        User user = findAuthenticatedUser(userId);
        Address address = userMapper.toAddressEntity(dto);
        address.setUser(user);
        return userMapper.toAddressDto(addressRepository.save(address));
    }

    @Transactional
    public AddressDto updateAddress(String userId, Long id, AddressDto dto) {
        Long ownerId = Long.valueOf(userId);
        Address address = addressRepository.findByIdAndUser_Id(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado."));
        userMapper.updateAddressFromDto(dto, address);
        return userMapper.toAddressDto(address);
    }

    public Long findUserIdByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getId();
    }

    @Transactional
    public void deleteByEmail(String email, String authenticatedUserId) {
        User user = findAuthenticatedUser(authenticatedUserId);
        if (!user.getEmail().equalsIgnoreCase(normalizeEmail(email))) {
            throw new AccessDeniedException("Você só pode excluir sua própria conta.");
        }
        userRepository.delete(user);
    }

    @Transactional
    public UserDto updateUser(String userId, UserUpdateDto dto) {
        User user = findAuthenticatedUser(userId);
        if (dto.name() != null) {
            if (dto.name().isBlank()) {
                throw new IllegalArgumentException("O nome não pode estar em branco.");
            }
            user.setName(dto.name().trim());
        }
        if (dto.email() != null) {
            if (dto.email().isBlank()) {
                throw new IllegalArgumentException("O e-mail não pode estar em branco.");
            }
            String email = normalizeEmail(dto.email());
            userRepository.findByEmailIgnoreCase(email).ifPresent(existing -> {
                if (!existing.getId().equals(user.getId())) {
                    throw new ConflictException("Este e-mail já está cadastrado.");
                }
            });
            user.setEmail(email);
        }
        if (dto.password() != null) {
            if (dto.password().isBlank()) {
                throw new IllegalArgumentException("A senha não pode estar em branco.");
            }
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email, String authenticatedUserId) {
        User user = findAuthenticatedUser(authenticatedUserId);
        if (!user.getEmail().equalsIgnoreCase(normalizeEmail(email))) {
            throw new AccessDeniedException("Você só pode consultar seus próprios dados.");
        }
        return userMapper.toDto(user);
    }

    private User findAuthenticatedUser(String userId) {
        try {
            return userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        } catch (NumberFormatException ex) {
            throw new AccessDeniedException("Identificação inválida.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
