package com.Ecommerce.Ecommerce.Dto;

import com.Ecommerce.Ecommerce.Validations.PasswordMatchesForCustomer;
import com.Ecommerce.Ecommerce.Validations.ValidPassword;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@PasswordMatchesForCustomer
public class CustomerRegisterDto
{
    @NotNull
    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;

    @NotNull
    @Pattern(regexp="(^$|[0-9]{10})", message = "Contact number must be of 10 digits")
    @NotBlank(message = "Contact number cannot be empty")
    private String contact;

    @NotNull
    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Valid
    @ValidPassword
    private String password;

    @NotNull (message = "Confirm Password cannot be empty")
    @Size(min = 8, max = 16, message = "Confirm Password should be same to Password")
    private String confirmPassword;

}
