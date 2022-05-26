package com.Ecommerce.Ecommerce.Exception;




public class EmailAlreadyConfirmedException extends RuntimeException
{
    public EmailAlreadyConfirmedException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public EmailAlreadyConfirmedException(String message)
    {
        super(message);
    }
}