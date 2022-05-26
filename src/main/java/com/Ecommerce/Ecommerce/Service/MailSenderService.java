package com.Ecommerce.Ecommerce.Service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
public class MailSenderService
{
    @Autowired
    private JavaMailSender javaMailSender;

    private String toEmail;
    private String subject;
    private String body;

    @Async
    public void sendEmail()
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("nafeesahmad1914@gmail.com");
        mailMessage.setTo(getToEmail());
        mailMessage.setSubject(getSubject());
        mailMessage.setText(getBody());
        javaMailSender.send(mailMessage);
    }
}
