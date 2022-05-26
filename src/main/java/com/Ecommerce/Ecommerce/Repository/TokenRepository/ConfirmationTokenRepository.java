package com.Ecommerce.Ecommerce.Repository.TokenRepository;

import com.Ecommerce.Ecommerce.Entity.Token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long>
{
    Optional<ConfirmationToken> findByToken(String token);

    @Query(value = "select * from confirmation_token ct where user_id = ?1",nativeQuery = true)
    ConfirmationToken findByUserId(Long id);

    @Transactional
    @Modifying
    @Query(value = "update confirmation_token ct" + "set ct.confirmedAt = ?1" + "where ct.token = ?2",nativeQuery = true)
    int updateConfirmedAt(LocalDateTime confirmedAt, String token);
}
