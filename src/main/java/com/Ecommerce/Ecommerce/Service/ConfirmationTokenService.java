package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Entity.Token.ConfirmationToken;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService
{
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;


    Optional<ConfirmationToken> getConfirmationToken(String token)
    {
        return confirmationTokenRepository.findByToken(token);
    }

    public int updateConfirmedAt(String token)
    {
        return confirmationTokenRepository.updateConfirmedAt(LocalDateTime.now(),token);
    }

    public void saveConfirmationToken(ConfirmationToken confirmationToken)
    {
        confirmationTokenRepository.save(confirmationToken);
    }
}
