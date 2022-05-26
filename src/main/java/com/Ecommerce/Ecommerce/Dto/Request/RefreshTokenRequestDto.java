package com.Ecommerce.Ecommerce.Dto.Request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RefreshTokenRequestDto
{
    @NotNull
    @NotBlank(message = "Refresh Token cannot be blank")
    private String refreshToken;
}
