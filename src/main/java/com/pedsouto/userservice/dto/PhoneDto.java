package com.pedsouto.userservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {

    private String number;
    private String areaCode;
}
