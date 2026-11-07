package com.pedsouto.userservice.controller;

import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.LoginDto;
import com.pedsouto.userservice.dto.UserUpdateDto;
import com.pedsouto.userservice.infra.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.util.Locale;
import com.pedsouto.userservice.dto.UserRegistrationDto;
import com.pedsouto.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/address")
    public ResponseEntity<AddressDto> addAddress(@Valid @RequestBody AddressDto dto,
                                                 Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAddress(authentication.getName(), dto));
    }

    @PutMapping("/address")
    public ResponseEntity<AddressDto> updateAddress(@RequestParam("id") Long id,
                                                    @Valid @RequestBody AddressDto dto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(userService.updateAddress(authentication.getName(), id, dto));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateDto dto,
                                              Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(authentication.getName(), dto));
    }

    @GetMapping
    public ResponseEntity<UserDto> findByEmail(@RequestParam("email") String email,
                                               Authentication authentication) {
        return ResponseEntity.ok(userService.findByEmail(email, authentication.getName()));
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email().trim().toLowerCase(Locale.ROOT), dto.password()));
        return "Bearer " + jwtUtil.generateToken(userService.findUserIdByEmail(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserRegistrationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(dto));
    }
}
