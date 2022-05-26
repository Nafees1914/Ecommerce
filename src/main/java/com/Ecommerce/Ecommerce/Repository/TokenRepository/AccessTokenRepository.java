package com.Ecommerce.Ecommerce.Repository.TokenRepository;

import com.Ecommerce.Ecommerce.Entity.Token.AccessToken;
import com.Ecommerce.Ecommerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken,Long>
{
    Optional<AccessToken> findByToken(String token);

    @Modifying
    @Transactional
    void deleteByUser(User user);

}
