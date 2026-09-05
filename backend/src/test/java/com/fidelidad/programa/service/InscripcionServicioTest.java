package com.fidelidad.programa.service;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fidelidad.programa.dto.InscripcionRequest;
import com.fidelidad.programa.entity.Ciudad;
import com.fidelidad.programa.entity.Departamento;
import com.fidelidad.programa.entity.Inscripcion;
import com.fidelidad.programa.entity.Marca;
import com.fidelidad.programa.entity.TipoIdentificacion;
import com.fidelidad.programa.exception.InscripcionDuplicadaException;
import com.fidelidad.programa.exception.RecursoNoEncontradoException;
import com.fidelidad.programa.repository.CiudadRepository;
import com.fidelidad.programa.repository.InscripcionRepository;
import com.fidelidad.programa.repository.MarcaRepository;
import com.fidelidad.programa.repository.TipoIdentificacionRepository;

@ExtendWith(MockitoExtension.class)
class InscripcionServicioTest {

    @Mock private InscripcionRepository inscripcionRepository;
    @Mock private TipoIdentificacionRepository tipoIdentificacionRepository;
    @Mock private CiudadRepository ciudadRepository;
    @Mock private MarcaRepository marcaRepository;

    private InscripcionServicio servicio;

    @BeforeEach
    void configurar() {
        servicio = new InscripcionServicio(
                inscripcionRepository, tipoIdentificacionRepository, ciudadRepository, marcaRepository);
    }

    private InscripcionRequest solicitudValida() {
        return new InscripcionRequest(
                1, "1020304050", "Juana", "Pérez",
                LocalDate.of(1995, 5, 20),
                "juana@example.com", "3001234567",
                "Calle 10 # 20-30", 1, null, 1);
    }

    @Test
    void registraCorrectamenteCuandoTodoEsValido() {
        TipoIdentificacion tipo = new TipoIdentificacion();
        tipo.setId(1);
        tipo.setCodigo("CC");
        tipo.setNombre("Cédula de ciudadanía");

        Departamento departamento = new Departamento();
        departamento.setId(1);
        departamento.setNombre("Boyacá");

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1);
        ciudad.setNombre("Tunja");
        ciudad.setDepartamento(departamento);

        Marca marca = new Marca();
        marca.setId(1);
        marca.setNombre("Chevignon");

        when(tipoIdentificacionRepository.findById(1)).thenReturn(Optional.of(tipo));
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca));
        when(ciudadRepository.findById(1)).thenReturn(Optional.of(ciudad));
        when(inscripcionRepository.existsByTipoIdentificacionIdAndNumeroIdentificacionAndMarcaId(
                anyInt(), anyString(), anyInt())).thenReturn(false);
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(invocacion -> {
            Inscripcion guardada = invocacion.getArgument(0);
            guardada.setId(1L);
            return guardada;
        });

        var respuesta = servicio.registrar(solicitudValida());

        assertThat(respuesta.id()).isEqualTo(1L);
        assertThat(respuesta.nombres()).isEqualTo("Juana");
        assertThat(respuesta.ciudad().nombre()).isEqualTo("Tunja");
    }

        @Test
    void rechazaCuandoYaExisteLaInscripcion() {
        TipoIdentificacion tipo = new TipoIdentificacion();
        tipo.setId(1);
        Marca marca = new Marca();
        marca.setId(1);
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1);

        when(tipoIdentificacionRepository.findById(1)).thenReturn(Optional.of(tipo));
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca));
        when(ciudadRepository.findById(1)).thenReturn(Optional.of(ciudad));
        when(inscripcionRepository.existsByTipoIdentificacionIdAndNumeroIdentificacionAndMarcaId(
                anyInt(), anyString(), anyInt())).thenReturn(true);

        assertThatThrownBy(() -> servicio.registrar(solicitudValida()))
                .isInstanceOf(InscripcionDuplicadaException.class);
    }

    @Test
    void rechazaCuandoElTipoDeIdentificacionNoExiste() {
        when(tipoIdentificacionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.registrar(solicitudValida()))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}