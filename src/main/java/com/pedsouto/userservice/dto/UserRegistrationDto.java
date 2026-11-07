package com.pedsouto.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.List;
public record UserRegistrationDto(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(min = 8) String password,
        List<@Valid AddressDto> addresses,
        List<@Valid PhoneDto> phones
) {
}
