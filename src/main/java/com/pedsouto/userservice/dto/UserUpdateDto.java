package com.pedsouto.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(max = 100) String name,
        @Email @Size(max = 100) String email,
        @Size(min = 8) String password
) {
}
