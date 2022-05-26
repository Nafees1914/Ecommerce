package com.Ecommerce.Ecommerce.Dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class LoginDto
{
    @NotNull
    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(message = "Enter correct password or else after 3rd attempt, account will be LOCKED!")
    private String password;
}
