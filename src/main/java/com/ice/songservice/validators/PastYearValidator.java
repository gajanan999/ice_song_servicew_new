package com.ice.songservice.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;


/**
 * Created a constraint validator
 */
public class PastYearValidator implements ConstraintValidator<PastYear, Integer> {
    @Override
    public void initialize(PastYear constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        if (year == null) {
            return true; // Let @NotNull handle null values
        }
        int currentYear = Year.now().getValue();
        return year <= currentYear;
    }
}
