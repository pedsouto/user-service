package com.pedsouto.userservice.controller;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.security.JwtUtil;
import com.pedsouto.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDto));
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getEmail(),
                        userDto.getPassword()
                )
        );

        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<UserDto> findByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteByEmail(@PathVariable("email") String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.updateUser(token, userDto));
    }

    @PutMapping("/address")
    public ResponseEntity<AddressDto> updateAddress(@RequestBody AddressDto addressDto,
                                                    @RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.updateAddress(id, addressDto));
    }

    @PutMapping("/phone")
    public ResponseEntity<PhoneDto> updatePhone(@RequestBody PhoneDto phoneDto,
                                                @RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.updatePhone(id, phoneDto));
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDto> addAddress(
            @RequestBody AddressDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.addAddress(token, dto));
    }

    @PostMapping("/phone")
    public ResponseEntity<PhoneDto> addPhone(
            @RequestBody PhoneDto dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.addPhone(token, dto));
    }
}
