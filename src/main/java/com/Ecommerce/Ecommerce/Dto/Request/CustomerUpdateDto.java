package com.Ecommerce.Ecommerce.Dto.Request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CustomerUpdateDto
{
    private String firstName;

    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Contact number must be of 10 digits")
    private String contact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    private String email;
}
