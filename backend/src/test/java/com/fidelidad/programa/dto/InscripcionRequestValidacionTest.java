package com.fidelidad.programa.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InscripcionRequestValidacionTest {

    private static Validator validador;

    @BeforeAll
    static void configurar() {
        validador = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private InscripcionRequest solicitudValida() {
        return new InscripcionRequest(
                1,
                "1020304050",
                "Juana",
                "Pérez",
                LocalDate.of(1995, 5, 20),
                "juana@example.com",
                "3001234567",
                "Calle 10 # 20-30",
                1,
                null,
                1);
    }

    @Test
    void solicitudCompletaYValidaNoTieneErrores() {
        Set<ConstraintViolation<InscripcionRequest>> violaciones = validador.validate(solicitudValida());
        assertThat(violaciones).isEmpty();
    }

    @Test
    void rechazaMenorDeEdad() {
        InscripcionRequest solicitud = new InscripcionRequest(
                1, "1020304050", "Juana", "Pérez",
                LocalDate.now().minusYears(10),
                null, null, "Calle 10 # 20-30", 1, null, 1);
        Set<ConstraintViolation<InscripcionRequest>> violaciones = validador.validate(solicitud);
        assertThat(violaciones).isNotEmpty();
    }

    @Test
    void rechazaDocumentoConSimbolos() {
        InscripcionRequest solicitud = new InscripcionRequest(
                1, "10-20-30", "Juana", "Pérez",
                LocalDate.of(1995, 5, 20),
                null, null, "Calle 10 # 20-30", 1, null, 1);
        Set<ConstraintViolation<InscripcionRequest>> violaciones = validador.validate(solicitud);
        assertThat(violaciones).isNotEmpty();
    }

    @Test
    void rechazaSinCiudadNiCiudadOtra() {
        InscripcionRequest solicitud = new InscripcionRequest(
                1, "1020304050", "Juana", "Pérez",
                LocalDate.of(1995, 5, 20),
                null, null, "Calle 10 # 20-30", null, null, 1);
        Set<ConstraintViolation<InscripcionRequest>> violaciones = validador.validate(solicitud);
        assertThat(violaciones).isNotEmpty();
    }

    @Test
    void aceptaCiudadOtraComoRespaldo() {
        InscripcionRequest solicitud = new InscripcionRequest(
                1, "1020304050", "Juana", "Pérez",
                LocalDate.of(1995, 5, 20),
                null, null, "Calle 10 # 20-30", null, "Mi pueblo no listado", 1);
        Set<ConstraintViolation<InscripcionRequest>> violaciones = validador.validate(solicitud);
        assertThat(violaciones).isEmpty();
    }

    @Test
    void rechazaNombreConNumeros() {
        InscripcionRequest solicitud = new InscripcionRequest(
                1, "1020304050", "Juana123", "Pérez",
                LocalDate.of(1995, 5, 20),
                null, null, "Calle 10 # 20-30", 1, null, 1);
        Set<ConstraintViolation<InscripcionRequest>> violaciones = validador.validate(solicitud);
        assertThat(violaciones).isNotEmpty();
    }
}