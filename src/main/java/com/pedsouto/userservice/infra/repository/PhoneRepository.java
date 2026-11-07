package com.pedsouto.userservice.infra.repository;

import com.pedsouto.userservice.infra.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByIdAndUser_Id(Long id, Long userId);
}
