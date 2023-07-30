package com.IdentityRegistry.IdentityRegistry.repository;

import com.IdentityRegistry.IdentityRegistry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
    User findByEmail(String email);
}
