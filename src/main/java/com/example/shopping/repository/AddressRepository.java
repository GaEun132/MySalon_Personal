package com.example.shopping.repository;

import com.example.shopping.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserUserId(Long userId);
    Optional<Address> findByUserUserIdAndIsDefaultTrue(Long userId);
}
