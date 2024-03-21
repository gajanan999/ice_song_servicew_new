package com.ice.songservice.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastYearValidator.class)
@Documented
public @interface PastYear {
    String message() default "Year must be in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}