package com.IdentityRegistry.IdentityRegistry.security.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

//    @Query("""
//            select t from Token t inner join User u on t.profileEntity.id = u.id
//            where u.id = :userId and (t.expired = false or t.revoked = false)
//            """)

    List<Token> findAllValidTokensById(Long userId);
    Optional<Token> findByToken(String token);
}
