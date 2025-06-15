package com.homework.library_management.dto.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class NotEmptyValidator implements ConstraintValidator<NotEmptyConstraint, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;
        return !value.trim().isEmpty();
    }
}
