package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Dto.Request.PasswordResetDto;
import com.Ecommerce.Ecommerce.Service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;


@RestController
@RequestMapping("/user")
public class ForgetPasswordController {

    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/forget-password")
    public ResponseEntity<?> forgotPassword(@Email @RequestParam String email) {
        return passwordResetTokenService.forgotPassword(email);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDtoDto) {
        return passwordResetTokenService.resetPassword(passwordResetDtoDto);
    }
}