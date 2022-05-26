package com.Ecommerce.Ecommerce.Dto.Request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SellerUpdateDto
{
    private String firstName;

    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Contact number must be of 10 digits")
    private String companyContact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    private String email;

    private String companyName;

    @Size(min = 15, max = 15)
    @Pattern(regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}", message = "GST number should be according to Indian Govt. Norms")
    private String gstNumber;
}
