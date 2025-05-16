package com.emobile.springtodo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;


/// Custom validator that validates whether a given string value is a valid constant from a specified enum class.
public class CheckEnumValidator implements ConstraintValidator<CheckEnum, String> {


    private Class<? extends Enum<?>> enumClass;
    private String message;

    /**
     * Initializes the validator with the enum class and error message defined in the {@link CheckEnum} annotation.
     *
     * @param constraintAnnotation the annotation that contains the parameters to initialize the validator
     */
    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.message = constraintAnnotation.message();
    }

    /**
     * Validates whether the provided value is a valid constant from the specified enum class.
     * <p>
     * The method checks if the provided value matches any of the constant names in the enum class. If the value
     * is invalid, it constructs a custom error message
     * </p>
     *
     * @param value the value to be validated
     * @param context the validation context
     * @return {@code true} if the value is a valid enum constant, otherwise {@code false}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .anyMatch(enumValue -> enumValue.equals(value));

        if (!valid) {
            String enumValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    message.replace("{enums}", enumValues)
            ).addConstraintViolation();
        }

        return valid;
    }
}