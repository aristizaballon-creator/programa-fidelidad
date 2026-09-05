package com.fidelidad.programa.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EdadMinimaValidator.class)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadMinima {

    int value() default 18;

    String message() default "No cumples con la edad mínima requerida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}