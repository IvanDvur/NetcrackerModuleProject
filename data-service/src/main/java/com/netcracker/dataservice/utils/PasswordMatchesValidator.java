package com.netcracker.dataservice.utils;

import com.netcracker.dataservice.dto.RegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

        @Override
        public void initialize(PasswordMatches constraintAnnotation) {
        }
        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context){
            RegistrationDto user = (RegistrationDto) obj;
            return user.getPassword().equals(user.getMatchingPassword());
        }
}
