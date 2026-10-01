package com.pedsouto.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

        private String name;
        private String email;
        private String password;
        private List<AddressDto> addresses;
        private List<PhoneDto> phones;
}
