package com.Ecommerce.Ecommerce.Dto.Request;

import com.Ecommerce.Ecommerce.Validations.PasswordMatchesForResetPasswordRequest;
import com.Ecommerce.Ecommerce.Validations.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordMatchesForResetPasswordRequest
public class PasswordResetDto
{
    @NotNull
    @NotBlank(message = "Password Reset Token cannot be blank")
    private String token;

    @NotNull
    @ValidPassword
    private String password;

    @NotNull
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Confirm Password should be same to Password")
    private String confirmPassword;

}
