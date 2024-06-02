package aw.geez.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthdayValidator implements ConstraintValidator<ValidBirthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isAfter(LocalDate.now().minusYears(14)) &&
                !value.isBefore(LocalDate.now().minusYears(120));
    }
}
