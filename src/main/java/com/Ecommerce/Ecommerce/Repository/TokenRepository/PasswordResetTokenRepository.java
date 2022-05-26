package com.Ecommerce.Ecommerce.Repository.TokenRepository;

import com.Ecommerce.Ecommerce.Entity.Token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>
{

    @Query(value = "select * from password_reset_token a where a.user_id = ?1", nativeQuery = true)
    PasswordResetToken existsByUserId(Long id);

    @Query(value = "select a.user_id from password_reset_token a where a.token = ?1", nativeQuery = true)
    Long findByUserId(String id);

    @Query(value = "select * from password_reset_token a where a.token = ?1" , nativeQuery = true)
    PasswordResetToken findByToken(String token);

}
