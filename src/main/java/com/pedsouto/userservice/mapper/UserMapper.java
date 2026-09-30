package com.pedsouto.userservice.mapper;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.entity.Address;
import com.pedsouto.userservice.infra.entity.Phone;
import com.pedsouto.userservice.infra.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    User toEntity(UserDto dto);

    UserDto toDto(User entity);

    void updateUserFromDto(UserDto dto, @MappingTarget User entity);

    Address toEntity(AddressDto dto);

    AddressDto toDto(Address entity);

    Phone toEntity(PhoneDto dto);

    PhoneDto toDto(Phone entity);

    List<Address> toAddressEntityList(List<AddressDto> dtos);

    List<AddressDto> toAddressDtoList(List<Address> entities);

    List<Phone> toPhoneEntityList(List<PhoneDto> dtos);

    List<PhoneDto> toPhoneDtoList(List<Phone> entities);
}
