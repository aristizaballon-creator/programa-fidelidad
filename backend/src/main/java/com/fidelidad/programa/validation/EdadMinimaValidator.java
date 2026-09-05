package com.fidelidad.programa.validation;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EdadMinimaValidator implements ConstraintValidator<EdadMinima, LocalDate> {

    private int edadMinima;

    @Override
    public void initialize(EdadMinima constraintAnnotation) {
        this.edadMinima = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            return true;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= edadMinima;
    }
}