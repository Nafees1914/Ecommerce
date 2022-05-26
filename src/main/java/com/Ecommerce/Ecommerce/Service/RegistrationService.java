package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Constant.SecurityConstants;
import com.Ecommerce.Ecommerce.Entity.Token.ConfirmationToken;
import com.Ecommerce.Ecommerce.Entity.User;
import com.Ecommerce.Ecommerce.Exception.EmailAlreadyConfirmedException;
import com.Ecommerce.Ecommerce.Exception.InvalidTokenException;
import com.Ecommerce.Ecommerce.Exception.TokenExpiredException;
import com.Ecommerce.Ecommerce.Repository.TokenRepository.ConfirmationTokenRepository;
import com.Ecommerce.Ecommerce.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class RegistrationService
{
    @Autowired
    ConfirmationTokenService confirmationTokenService;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailSender mailSender;
    @Autowired
    UserServiceImpl userServiceimpl;

    public String generateToken(User user)
    {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusSeconds(SecurityConstants.CONFIRMATION_TOKEN_EXPIRATION));
        confirmationToken.setUser(user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public String confirmByToken(String token)
    {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getConfirmationToken(token)
                .orElseThrow(()->new InvalidTokenException("Token not found!"));

        if(confirmationToken.getConfirmedAt() != null)
        {
            throw new EmailAlreadyConfirmedException("Email is already confirmed!");
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            throw new TokenExpiredException("Token given is expired!");
        }

        User user = userRepository.findUserByEmail(confirmationToken.getUser().getEmail());
        if(user!=null)
        {
            user.setActive(true);
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("nafeesahmad1914@gmail.com");
        mailMessage.setTo(confirmationToken.getUser().getEmail());
        mailMessage.setSubject("Account Activated");
        mailMessage.setText("Your account has been activated. Enjoy shopping");
        mailMessage.setSentDate(new Date());

        try {
            mailSender.send(mailMessage);
        }
        catch(MailException e)
        {
            log.info("Error sending mail");
        }
        return "Account is activated";
    }

    public String reactivationByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        boolean userExists = userRepository.findByEmail(user.get().getEmail()).isPresent();

        if(userExists)
        {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByUserId(user.get().getId());
            if(confirmationToken.getConfirmedAt() !=null)
            {
                throw new EmailAlreadyConfirmedException("Email already confirmed");
            }

            String generatedToken = generateToken(user.get());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("nafeesahmad1914@gmail.com");
            mailMessage.setTo(confirmationToken.getUser().getEmail());
            mailMessage.setSubject("Re-Activation Link");
            mailMessage.setText("New activation link is only valid for 15 minutes.\n"+
                    "http://localhost:8080/api/confirm?token="+ generatedToken);
            mailMessage.setSentDate(new Date());

            try
            {
                mailSender.send(mailMessage);
            }
            catch(MailException e)
            {
                log.info("Error sending mail");
            }
            return "Re-Activation link sent to your email";

        }
        return "Failed to send Email";
    }

    // Service Method for Admin Controller

    public ResponseEntity<?> activateById(Long id, HttpServletRequest request) {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        System.out.println("a");
        System.out.println(userRepository.findById(id).get().getEmail());
        if (userRepository.existsById(id)) {
            log.info("User exists.");
            if (userRepository.isUserActive(id))
            {
                return ResponseEntity.ok("Already confirmed User.");
            }
            else
            {
                User user = userRepository.getById(id);
                user.setActive(true);
                userRepository.save(user);

                //Mail Part
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setFrom("nafeesahmad1914@gmail.com");
                mailMessage.setSubject("Account Activated by Admin");
                mailMessage.setText("Your account has been activated by Admin. Enjoy.");
                mailMessage.setSentDate(new Date());

                try
                {
                    mailSender.send(mailMessage);
                } catch (MailException e) {
                    log.info("Error sending mail");
                }
                log.info("User activated!!");
            }
        } else {
            log.info("No User exists!!");
            return ResponseEntity.badRequest().body(String.format("No user exists with this user id: %s", id));
        }
        return ResponseEntity.ok().body(String.format("User activated with this user id: %s", id));
    }


    public ResponseEntity<?> deactivateById(Long id, HttpServletRequest request)
    {
        String token= userServiceimpl.getJwtFromRequest(request);
        String email = userServiceimpl.getUserNameFromJwtToken(token);
        if (userRepository.existsById(id))
        {
            log.info("User exists.");
            if (!userRepository.isUserActive(id))
            {
                return ResponseEntity.ok("Already de-activated User.");
            }
            else
            {
                User user = userRepository.getById(id);
                user.setActive(false);
                userRepository.save(user);

                //Mail Part
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("nafeesahmad1914@gmail.com");
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Account De-activated by Admin");
                mailMessage.setText("Your account has been de-activated by Admin.\n Kindly contact admin to activate your account, Thanks.");
                mailMessage.setSentDate(new Date());
                try
                {
                    mailSender.send(mailMessage);
                }
                catch (MailException e)
                {
                    log.info("Error sending mail");
                }
                log.info("User de-activated!!");
            }
        }
        else
        {
            log.info("No User exists!!");
            return ResponseEntity.badRequest().body(String.format("No user exists with this user id: %s", id));
        }
        return ResponseEntity.ok().body(String.format("User de-activated with this user id: %s", id));
    }

}
