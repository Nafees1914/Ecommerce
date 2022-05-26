package com.Ecommerce.Ecommerce.Dto.Request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateAddressDto
{

    private String addressLine;

    private String city;

    private String state;

    private String country;

    @Pattern(regexp="(^$|[0-9]{6})", message = "It should be exact 6 digits")
    @Size(min = 6, max = 6, message = "It should be exact 6 digits")
    private String zipcode;

    private String label;
}
