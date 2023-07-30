package com.IdentityRegistry.IdentityRegistry.repository;

import com.IdentityRegistry.IdentityRegistry.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleName(String roleName);

}
