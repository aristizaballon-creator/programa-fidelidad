package com.fidelidad.programa.dto;

import java.time.LocalDate;

public record InscripcionResponse(
        Long id,
        TipoIdentificacionDto tipoIdentificacion,
        String numeroIdentificacion,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String email,
        String telefono,
        String direccion,
        CiudadDto ciudad,
        String ciudadOtra,
        MarcaDto marca
) {
}