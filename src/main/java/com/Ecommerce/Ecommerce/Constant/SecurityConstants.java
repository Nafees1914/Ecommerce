package com.Ecommerce.Ecommerce.Constant;

public class SecurityConstants
{
    public static final long CONFIRMATION_TOKEN_EXPIRATION = 1800;
    public static final long JWT_EXPIRATION = 1000*60*60;
    public static  final  long JWT_REFRESH_EXPIRATION = 24;
    public static  final  long JWT_LOGIN_EXPIRATION = 20;
    public static  final  long JWT_RESET_EXPIRATION = 15;
    public static  final  long EXPIRE_TOKEN_AFTER_MINUTES = 30;
    public static final String JWT_SECRET = "secretGames";
}
