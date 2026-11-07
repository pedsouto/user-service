package com.pedsouto.userservice.dto;

public record UserDto(
        Long id,
        String name,
        String email
) {
}
