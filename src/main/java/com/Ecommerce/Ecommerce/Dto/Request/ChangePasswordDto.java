package com.Ecommerce.Ecommerce.Dto.Request;

import com.Ecommerce.Ecommerce.Validations.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordDto
{

    @NotNull (message = "Password cannot be empty")
    @ValidPassword
    private String password;

    @NotNull (message = "Confirm Password cannot be empty")
    @NotBlank(message = "Confirm Password should be same to Password")
    private String confirmPassword;
}
