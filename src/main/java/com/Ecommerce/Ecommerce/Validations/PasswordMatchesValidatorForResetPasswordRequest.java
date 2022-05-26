package com.Ecommerce.Ecommerce.Validations;

import com.Ecommerce.Ecommerce.Dto.Request.PasswordResetDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidatorForResetPasswordRequest implements ConstraintValidator<PasswordMatchesForResetPasswordRequest, Object> {

    @Override
    public void initialize(final PasswordMatchesForResetPasswordRequest constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final PasswordResetDto user = (PasswordResetDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}
