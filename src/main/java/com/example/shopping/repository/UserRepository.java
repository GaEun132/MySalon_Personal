package com.example.shopping.repository;


import com.example.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(String id);

    Optional<User> findByUserId(Long userId);
    boolean existsById(String id);
    boolean existsByUserId(Long userId);
    boolean existsByUserName(String userName);

}
