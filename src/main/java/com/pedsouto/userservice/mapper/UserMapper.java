package com.pedsouto.userservice.mapper;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.entity.Address;
import com.pedsouto.userservice.infra.entity.Phone;
import com.pedsouto.userservice.infra.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    User toUserEntity(UserDto dto);

    UserDto toUserDto(User entity);

    void updateUserFromDto(UserDto dto, @MappingTarget User entity);

    Address toAddressEntity(AddressDto dto);

    Address toAddressEntity(AddressDto dto, Long userId);

    AddressDto toAddressDto(Address entity);

    void updateAddressFromDto(AddressDto dto, @MappingTarget Address entity);

    List<Address> toAddressEntityList(List<AddressDto> dtos);

    List<AddressDto> toAddressDtoList(List<Address> entities);

    Phone toPhoneEntity(PhoneDto dto);

    Phone toPhoneEntity(PhoneDto dto, Long userId);

    PhoneDto toPhoneDto(Phone entity);

    void updatePhoneFromDto(PhoneDto dto, @MappingTarget Phone entity);

    List<Phone> toPhoneEntityList(List<PhoneDto> dtos);

    List<PhoneDto> toPhoneDtoList(List<Phone> entities);
}
