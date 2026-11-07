package com.pedsouto.userservice.infra.repository;

import com.pedsouto.userservice.infra.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIdAndUser_Id(Long id, Long userId);
}
