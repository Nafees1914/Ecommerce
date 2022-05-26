package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Dto.Request.PasswordResetDto;
import com.Ecommerce.Ecommerce.Entity.Token.PasswordResetToken;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.PasswordResetTokenRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class PasswordResetTokenService
{
    @Autowired
    MailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    public ResponseEntity<?> forgotPassword(String email)
    {
        if(userRepository.existsByEmail(email))
        {
            return forgotPasswordUtility(email);
        }
        else
        {
            return new ResponseEntity<>("No user exists with this Email ID!", HttpStatus.NOT_FOUND);
        }
    }

    public String generateToken(User user)
    {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setCreatedAt(LocalDateTime.now());
        passwordResetToken.setExpireAt(LocalDateTime.now().plusMinutes(SecurityConstants.JWT_RESET_EXPIRATION));
        passwordResetToken.setUser(user);

        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    public ResponseEntity<?> forgotPasswordUtility(String email)
    {

        User user = userRepository.findUserByEmail(email);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.existsByUserId(user.getId());

        if (passwordResetToken == null)
        {
            String token = generateToken(user);
            log.info("password reset token generated");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("nafeesahmad1914@gmail.com.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Reset Password Link");
            mailMessage.setText("You seriously forgot your password\nIt's okay we got you...,\n Use below link to reset the password within 15 minutes."
                    +"\nhttp://localhost:8080/api/user/reset-password?token="+token
                    +"\nLink will expire after 15 minutes."
                    +"\nEnjoy.");
            mailMessage.setSentDate(new Date());

            try
            {
                mailSender.send(mailMessage);
                log.info("mail sent");
                log.info(token);
            }
            catch(MailException e)
            {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Generated new Password Reset Token, sending to your mailbox", HttpStatus.OK);
        }
        else
        {
            passwordResetTokenRepository.deleteById(passwordResetToken.getId());
            log.info("deleted password reset token");
            String token = generateToken(user);
            log.info("password reset token generated");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("nafeesahmad1914@gmail.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Reset Password Link");
            mailMessage.setText("You seriously forgot your password\nIt's okay we got you...,\n Use below link to reset the password within 15 minutes."
                    +"\nhttp://localhost:8080/api/user/reset-password?token="+token
                    +"\nLink will expire after 15 minutes."
                    +"\nEnjoy.");
            mailMessage.setSentDate(new Date());

            try
            {
                mailSender.send(mailMessage);
                log.info("mail sent");
                log.info(token);
            }
            catch(MailException e)
            {
                log.info("Error sending mail");
            }

            return new ResponseEntity<>("Existing Password Reset Token deleted and created new one\n" +
                    "check your mailbox.", HttpStatus.OK);
        }
    }


    public ResponseEntity<?> resetPassword(PasswordResetDto passwordResetDto)
    {

        User user = userRepository.findById(passwordResetTokenRepository.findByUserId(passwordResetDto.getToken())).get();
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(passwordResetDto.getToken());
        if (passwordResetToken == null)
        {
            log.info("no token found");
            return new ResponseEntity<>("Invalid Token!", HttpStatus.BAD_REQUEST);
        }
        else
        {
            log.info("token found");
            if(isTokenExpired(passwordResetToken.getExpireAt())) {
                log.info("expired token");
                passwordResetTokenRepository.delete(passwordResetToken);
                return new ResponseEntity<>("Token has been expired!", HttpStatus.BAD_REQUEST);
            }
            else
            {
                user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
                log.info("Password Changed");
                userRepository.save(user);
                passwordResetTokenRepository.delete(passwordResetToken);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("nafeesahmad1914@gmail.com");
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Password Reset");
                mailMessage.setText("ALERT!, Your account password has been reset, If it was not you contact Admin asap.\nStay Safe, Thanks.");


                mailMessage.setSentDate(new Date());
                try {
                    log.info("mail sent");
                    mailSender.send(mailMessage);
                } catch (MailException e) {
                    log.info("Error sending mail");
                }
                return new ResponseEntity<>("Password Changed and deleted token", HttpStatus.OK);
            }
        }
    }

    public boolean isTokenExpired(LocalDateTime tokenCreationDate)
    {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate,now);

        return diff.toMinutes() >= SecurityConstants.EXPIRE_TOKEN_AFTER_MINUTES;

    }
}
