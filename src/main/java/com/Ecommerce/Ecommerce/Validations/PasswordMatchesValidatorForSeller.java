package com.Ecommerce.Ecommerce.Validations;

import com.Ecommerce.Ecommerce.Dto.SellerRegisterDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidatorForSeller implements ConstraintValidator<PasswordMatchesForSeller, Object> {

    @Override
    public void initialize(final PasswordMatchesForSeller constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final SellerRegisterDto user = (SellerRegisterDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}
