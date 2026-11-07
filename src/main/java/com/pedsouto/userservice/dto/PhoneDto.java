package com.pedsouto.userservice.dto;

import jakarta.validation.constraints.Size;

public record PhoneDto(
        Long id,
        @Size(max = 5) String areaCode,
        @Size(max = 20) String number
) {
}
