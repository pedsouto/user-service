package com.pedsouto.userservice.service;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.entity.Address;
import com.pedsouto.userservice.infra.entity.Phone;
import com.pedsouto.userservice.infra.entity.User;
import com.pedsouto.userservice.infra.exception.ConflictException;
import com.pedsouto.userservice.infra.exception.ResourceNotFoundException;
import com.pedsouto.userservice.infra.repository.AddressRepository;
import com.pedsouto.userservice.infra.repository.PhoneRepository;
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
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        validateEmail(userDto.getEmail());

        User user = userMapper.toUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return userMapper.toUserDto(user);
    }

    @Transactional
    public void deleteByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public UserDto updateUser(String token, UserDto userDto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        userDto.setPassword(userDto.getPassword() != null ? passwordEncoder.encode(userDto.getPassword()) : null);

        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        userMapper.updateUserFromDto(userDto, userEntity);

        return userMapper.toUserDto(userRepository.save(userEntity));
    }

    @Transactional
    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        Address addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        userMapper.updateAddressFromDto(addressDto, addressEntity);

        return userMapper.toAddressDto(addressRepository.save(addressEntity));
    }

    @Transactional
    public PhoneDto updatePhone(Long id, PhoneDto phoneDto) {
        Phone phoneEntity = phoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found with id: " + id));

        userMapper.updatePhoneFromDto(phoneDto, phoneEntity);

        return userMapper.toPhoneDto(phoneRepository.save(phoneEntity));
    }

    @Transactional
    public AddressDto addAddress(String token, AddressDto dto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Address address = userMapper.toAddressEntity(dto, user.getId());
        Address savedAddress = addressRepository.save(address);

        return userMapper.toAddressDto(savedAddress);
    }

    @Transactional
    public PhoneDto addPhone(String token, PhoneDto dto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Phone phone = userMapper.toPhoneEntity(dto, user.getId());
        Phone savedPhone = phoneRepository.save(phone);

        return userMapper.toPhoneDto(savedPhone);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already registered: " + email);
        }
    }
}
