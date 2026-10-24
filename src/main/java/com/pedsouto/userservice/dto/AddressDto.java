package com.pedsouto.userservice.dto;

import lombok.Builder;

@Builder
public record AddressDto(
        Long id,
        String street,
        String number,
        String complement,
        String city,
        String state,
        String zipCode
) {
}
