package com.Ecommerce.Ecommerce.Dto;


import lombok.*;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken;

    public AuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}