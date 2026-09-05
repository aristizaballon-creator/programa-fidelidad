package com.fidelidad.programa.validation;

import com.fidelidad.programa.dto.InscripcionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CiudadValidaValidator implements ConstraintValidator<CiudadValida, InscripcionRequest> {

    @Override
    public boolean isValid(InscripcionRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }
        boolean tieneCiudadId = request.ciudadId() != null;
        boolean tieneCiudadOtra = request.ciudadOtra() != null && !request.ciudadOtra().isBlank();
        if (tieneCiudadId || tieneCiudadOtra) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("ciudadId")
                .addConstraintViolation();
        return false;
    }
}