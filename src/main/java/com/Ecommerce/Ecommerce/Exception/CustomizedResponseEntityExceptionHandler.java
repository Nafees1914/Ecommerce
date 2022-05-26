package com.Ecommerce.Ecommerce.Exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<Object> handleTokenNotFoundException(InvalidTokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {EmailAlreadyConfirmedException.class})
    public ResponseEntity<Object> handleEmailAlreadyConfirmedException(EmailAlreadyConfirmedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFound(ObjectNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleForbiddenExceptionExceptions(AccessDeniedException e) {

        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors() // returns a list of errors
                .forEach(fieldError -> {
                    // populating map with, field with error as key, and relevant message as value
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());

                });

       return this.handleExceptionInternal(ex, errorMap, headers, status, request);

    }
}
