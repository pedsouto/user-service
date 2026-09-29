package com.pedsouto.userservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private String street;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String zipCode;
}
