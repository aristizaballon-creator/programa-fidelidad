package com.fidelidad.programa.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidelidad.programa.dto.InscripcionRequest;
import com.fidelidad.programa.entity.Ciudad;
import com.fidelidad.programa.entity.Departamento;
import com.fidelidad.programa.entity.Marca;
import com.fidelidad.programa.entity.Pais;
import com.fidelidad.programa.entity.TipoIdentificacion;
import com.fidelidad.programa.repository.CiudadRepository;
import com.fidelidad.programa.repository.DepartamentoRepository;
import com.fidelidad.programa.repository.MarcaRepository;
import com.fidelidad.programa.repository.PaisRepository;
import com.fidelidad.programa.repository.TipoIdentificacionRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InscripcionControladorIntegracionTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TipoIdentificacionRepository tipoIdentificacionRepository;
    @Autowired private PaisRepository paisRepository;
    @Autowired private DepartamentoRepository departamentoRepository;
    @Autowired private CiudadRepository ciudadRepository;
    @Autowired private MarcaRepository marcaRepository;

    private Integer tipoId;
    private Integer ciudadId;
    private Integer marcaId;

    @BeforeAll
        @SuppressWarnings("unused")
    void sembrarCatalogos() {
        TipoIdentificacion tipo = new TipoIdentificacion();
        tipo.setCodigo("CC");
        tipo.setNombre("Cédula de ciudadanía");
        tipoId = tipoIdentificacionRepository.save(tipo).getId();

        Pais pais = new Pais();
        pais.setNombre("Colombia");
        pais.setCodigoIso2("CO");
        pais = paisRepository.save(pais);

        Departamento departamento = new Departamento();
        departamento.setNombre("Boyacá");
        departamento.setPais(pais);
        departamento = departamentoRepository.save(departamento);

        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Tunja");
        ciudad.setDepartamento(departamento);
        ciudadId = ciudadRepository.save(ciudad).getId();

        Marca marca = new Marca();
        marca.setNombre("Chevignon");
        marca.setActiva(true);
        marcaId = marcaRepository.save(marca).getId();
    }

    private InscripcionRequest solicitud(String numeroDocumento) {
        return new InscripcionRequest(
                tipoId, numeroDocumento, "Juana", "Pérez",
                LocalDate.of(1995, 5, 20), null, null,
                "Calle 10 # 20-30", ciudadId, null, marcaId);
    }

        private String json(InscripcionRequest solicitud) {
                return "{" +
                                "\"tipoIdentificacionId\":" + solicitud.tipoIdentificacionId() + "," +
                                "\"numeroIdentificacion\":" + valorJson(solicitud.numeroIdentificacion()) + "," +
                                "\"nombres\":" + valorJson(solicitud.nombres()) + "," +
                                "\"apellidos\":" + valorJson(solicitud.apellidos()) + "," +
                                "\"fechaNacimiento\":" + valorJson(solicitud.fechaNacimiento().toString()) + "," +
                                "\"email\":" + valorJson(solicitud.email()) + "," +
                                "\"telefono\":" + valorJson(solicitud.telefono()) + "," +
                                "\"direccion\":" + valorJson(solicitud.direccion()) + "," +
                                "\"ciudadId\":" + solicitud.ciudadId() + "," +
                                "\"ciudadOtra\":" + valorJson(solicitud.ciudadOtra()) + "," +
                                "\"marcaId\":" + solicitud.marcaId() +
                                "}";
        }

        private String valorJson(String valor) {
                return valor == null ? "null" : "\"" + valor + "\"";
        }

    @Test
    void catalogoDeMarcasRespondeConLaSembrada() throws Exception {
        mockMvc.perform(get("/api/catalogos/marcas"))
                .andExpect(status().isOk());
    }

    @Test
    void creaInscripcionValidaCorrectamente() throws Exception {
        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(solicitud("9000001"))))
                .andExpect(status().isCreated());
    }

    @Test
    void rechazaInscripcionDuplicada() throws Exception {
        String solicitud = json(solicitud("9000002"));
        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(solicitud));
        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(solicitud))
                .andExpect(status().isConflict());
    }

    @Test
    void rechazaMenorDeEdad() throws Exception {
        InscripcionRequest menor = new InscripcionRequest(
                tipoId, "9000003", "Menor", "De Edad",
                LocalDate.now().minusYears(10), null, null,
                "Calle falsa 123", ciudadId, null, marcaId);
        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(menor)))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void aceptaCiudadOtraComoRespaldo() throws Exception {
        InscripcionRequest conCiudadOtra = new InscripcionRequest(
                tipoId, "9000004", "Sin", "Ciudad",
                LocalDate.of(1990, 1, 1), null, null,
                "Calle falsa 456", null, "Mi pueblo no listado", marcaId);
        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(conCiudadOtra)))
                .andExpect(status().isCreated());
    }
}