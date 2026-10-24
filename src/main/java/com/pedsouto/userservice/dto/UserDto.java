package com.pedsouto.userservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(
        String name,
        String email,
        String password,
        List<AddressDto> addresses,
        List<PhoneDto> phones
) {
}
