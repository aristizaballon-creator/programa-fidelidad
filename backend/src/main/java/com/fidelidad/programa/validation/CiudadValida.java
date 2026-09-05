package com.fidelidad.programa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CiudadValidaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CiudadValida {

    String message() default "Selecciona una ciudad del catálogo o escribe el nombre de tu ciudad";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}