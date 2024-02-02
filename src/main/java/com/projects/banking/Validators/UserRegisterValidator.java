package com.projects.banking.Validators;

import com.projects.banking.Entities.UserEntity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRegisterValidator implements ConstraintValidator<ValidUserRegister, UserEntity> {
    @Override
    public void initialize(ValidUserRegister constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserEntity userEntity, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
