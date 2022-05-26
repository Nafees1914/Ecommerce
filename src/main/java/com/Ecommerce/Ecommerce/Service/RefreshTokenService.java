package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Entity.Token.RefreshToken;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Exception.TokenRefreshException;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.RefreshTokenRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService
{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    public RefreshToken generateRefreshToken(Long id)
    {
        User user =userRepository.findById(id).get();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setEmail(user.getEmail());
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpireAt(LocalDateTime.now().plusHours(SecurityConstants.JWT_REFRESH_EXPIRATION));
        refreshToken.setUser(user);

        RefreshToken token = refreshTokenRepository.save(refreshToken);
        return token;
    }


    public RefreshToken checkExpiration(RefreshToken token) {
        if (token.getExpireAt().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token)
    {
        return refreshTokenRepository.findByToken(token);
    }
}
