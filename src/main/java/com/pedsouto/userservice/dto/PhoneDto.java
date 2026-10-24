package com.pedsouto.userservice.dto;

import lombok.Builder;

@Builder
public record PhoneDto(
        Long id,
        String areaCode,
        String number
) {
}
