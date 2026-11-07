package com.pedsouto.userservice.dto;

import jakarta.validation.constraints.Size;

public record AddressDto(
        Long id,
        @Size(max = 255) String street,
        @Size(max = 50) String number,
        @Size(max = 50) String complement,
        @Size(max = 150) String city,
        @Size(max = 2) String state,
        @Size(max = 9) String zipCode
) {
}
