package com.Ecommerce.Ecommerce.Exception;

public class TokenExpiredException extends RuntimeException
{
    public TokenExpiredException(String message)
    {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause)
    {
        super(message,cause);
    }
}
