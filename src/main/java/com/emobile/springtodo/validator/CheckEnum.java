package com.emobile.springtodo.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation to ensure that a given field or parameter is a valid  enum type.
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckEnumValidator.class)
public @interface CheckEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "must be any of enum: {enums}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
