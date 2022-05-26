package com.Ecommerce.Ecommerce.Repository.TokenRepository;

import com.Ecommerce.Ecommerce.Entity.Token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>
{
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findById(Long id);
    @Modifying
    void deleteByToken(String token);

}
