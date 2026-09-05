package com.fidelidad.programa.dto;

import java.time.LocalDate;

import com.fidelidad.programa.validation.CiudadValida;
import com.fidelidad.programa.validation.EdadMinima;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@CiudadValida
public record InscripcionRequest(

        @NotNull(message = "Selecciona un tipo de identificación")
        Integer tipoIdentificacionId,

        @NotBlank(message = "Ingresa tu número de identificación")
        @Pattern(regexp = "^[A-Za-z0-9]{5,20}$", message = "El número de identificación debe tener entre 5 y 20 caracteres alfanuméricos")
        String numeroIdentificacion,

        @NotBlank(message = "Ingresa tus nombres")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\\s']{2,100}$", message = "Solo se permiten letras y espacios")
        String nombres,

        @NotBlank(message = "Ingresa tus apellidos")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\\s']{2,100}$", message = "Solo se permiten letras y espacios")
        String apellidos,

        @NotNull(message = "Ingresa tu fecha de nacimiento")
        @Past(message = "La fecha de nacimiento no puede ser una fecha futura")
        @EdadMinima(value = 18, message = "Debes ser mayor de 18 años para inscribirte al programa de fidelidad")
        LocalDate fechaNacimiento,

        @Email(message = "Ingresa un correo electrónico válido")
        String email,

        @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$", message = "Ingresa un teléfono válido")
        String telefono,

        @NotBlank(message = "Ingresa tu dirección")
        @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
        String direccion,

        Integer ciudadId,

        @Size(max = 150, message = "El nombre de la ciudad es demasiado largo")
        String ciudadOtra,

        @NotNull(message = "Selecciona una marca")
        Integer marcaId

) {
}