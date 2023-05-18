package com.netcracker.dataservice.utils;

import com.netcracker.dataservice.security.RegistrationForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

        @Override
        public void initialize(PasswordMatches constraintAnnotation) {
        }
        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context){
            RegistrationForm user = (RegistrationForm) obj;
            return user.getPassword().equals(user.getMatchingPassword());
        }
}
