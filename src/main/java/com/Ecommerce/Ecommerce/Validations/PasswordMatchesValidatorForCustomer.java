package com.Ecommerce.Ecommerce.Validations;

import com.Ecommerce.Ecommerce.Dto.CustomerRegisterDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidatorForCustomer implements ConstraintValidator<PasswordMatchesForCustomer, Object> {

    @Override
    public void initialize(final PasswordMatchesForCustomer constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {

        final CustomerRegisterDto user = (CustomerRegisterDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}