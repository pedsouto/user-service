package com.pedsouto.userservice.mapper;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.dto.UserRegistrationDto;
import com.pedsouto.userservice.infra.entity.Address;
import com.pedsouto.userservice.infra.entity.Phone;
import com.pedsouto.userservice.infra.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "phones", ignore = true)
    User toEntity(UserRegistrationDto dto);

    UserDto toDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toAddressEntity(AddressDto dto);

    AddressDto toAddressDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Phone toPhoneEntity(PhoneDto dto);

    PhoneDto toPhoneDto(Phone phone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updatePhoneFromDto(PhoneDto dto, @MappingTarget Phone phone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromDto(AddressDto dto, @MappingTarget Address address);
}
