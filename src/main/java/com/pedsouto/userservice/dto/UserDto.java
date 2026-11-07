package com.pedsouto.userservice.dto;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        String email,
        List<AddressDto> addresses
) {
}
