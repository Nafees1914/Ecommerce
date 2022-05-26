package com.Ecommerce.Ecommerce.Dto;

import com.Ecommerce.Ecommerce.Validations.ValidPassword;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
public class SellerRegisterDto
{
    @NotNull
    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;

    @NotNull
    @NotBlank(message = "Company name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Company name should be unique")
    private String companyName;

    @NotNull
    @Pattern(regexp="(^$|[0-9]{10})", message = "Company contact number must be of 10 digits")
    @NotBlank(message = "Company contact number cannot be empty")
    private String companyContact;

    @NotNull
    @Size(min = 15, max = 15)
    @Pattern(regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}", message = "GST number should be according to Indian Govt. Norms")
    @NotBlank(message = "GST number cannot be empty")
    private String gstNumber;

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
