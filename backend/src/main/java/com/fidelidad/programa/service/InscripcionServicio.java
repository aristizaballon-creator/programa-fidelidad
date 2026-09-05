package com.fidelidad.programa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelidad.programa.dto.CiudadDto;
import com.fidelidad.programa.dto.InscripcionRequest;
import com.fidelidad.programa.dto.InscripcionResponse;
import com.fidelidad.programa.dto.MarcaDto;
import com.fidelidad.programa.dto.TipoIdentificacionDto;
import com.fidelidad.programa.entity.Ciudad;
import com.fidelidad.programa.entity.Inscripcion;
import com.fidelidad.programa.entity.Marca;
import com.fidelidad.programa.entity.TipoIdentificacion;
import com.fidelidad.programa.exception.InscripcionDuplicadaException;
import com.fidelidad.programa.exception.RecursoNoEncontradoException;
import com.fidelidad.programa.repository.CiudadRepository;
import com.fidelidad.programa.repository.InscripcionRepository;
import com.fidelidad.programa.repository.MarcaRepository;
import com.fidelidad.programa.repository.TipoIdentificacionRepository;

@Service
public class InscripcionServicio {

    private final InscripcionRepository inscripcionRepository;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;
    private final CiudadRepository ciudadRepository;
    private final MarcaRepository marcaRepository;

    public InscripcionServicio(
            InscripcionRepository inscripcionRepository,
            TipoIdentificacionRepository tipoIdentificacionRepository,
            CiudadRepository ciudadRepository,
            MarcaRepository marcaRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.ciudadRepository = ciudadRepository;
        this.marcaRepository = marcaRepository;
    }

    @Transactional
    public InscripcionResponse registrar(InscripcionRequest request) {
        TipoIdentificacion tipoIdentificacion = tipoIdentificacionRepository
                .findById(request.tipoIdentificacionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El tipo de identificación seleccionado no existe"));

        Marca marca = marcaRepository.findById(request.marcaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("La marca seleccionada no existe"));

        Ciudad ciudad = null;
        if (request.ciudadId() != null) {
            ciudad = ciudadRepository.findById(request.ciudadId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("La ciudad seleccionada no existe"));
        }

        boolean yaExiste = inscripcionRepository.existsByTipoIdentificacionIdAndNumeroIdentificacionAndMarcaId(
                request.tipoIdentificacionId(), request.numeroIdentificacion(), request.marcaId());
        if (yaExiste) {
            throw new InscripcionDuplicadaException(
                    "Este número de identificación ya está inscrito en el programa de fidelidad de la marca seleccionada");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setTipoIdentificacion(tipoIdentificacion);
        inscripcion.setNumeroIdentificacion(request.numeroIdentificacion());
        inscripcion.setNombres(request.nombres());
        inscripcion.setApellidos(request.apellidos());
        inscripcion.setFechaNacimiento(request.fechaNacimiento());
        inscripcion.setEmail(request.email());
        inscripcion.setTelefono(request.telefono());
        inscripcion.setDireccion(request.direccion());
        inscripcion.setCiudad(ciudad);
        inscripcion.setCiudadOtra(request.ciudadOtra());
        inscripcion.setMarca(marca);

        Inscripcion guardada = inscripcionRepository.save(inscripcion);
        return mapearResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponse> listar() {
        return inscripcionRepository.findAll().stream()
                .map(this::mapearResponse)
                .toList();
    }

    private InscripcionResponse mapearResponse(Inscripcion inscripcion) {
        TipoIdentificacionDto tipoDto = new TipoIdentificacionDto(
                inscripcion.getTipoIdentificacion().getId(),
                inscripcion.getTipoIdentificacion().getCodigo(),
                inscripcion.getTipoIdentificacion().getNombre());

        CiudadDto ciudadDto = null;
        if (inscripcion.getCiudad() != null) {
            ciudadDto = new CiudadDto(
                    inscripcion.getCiudad().getId(),
                    inscripcion.getCiudad().getNombre(),
                    inscripcion.getCiudad().getDepartamento().getId());
        }

        MarcaDto marcaDto = new MarcaDto(inscripcion.getMarca().getId(), inscripcion.getMarca().getNombre());

        return new InscripcionResponse(
                inscripcion.getId(),
                tipoDto,
                inscripcion.getNumeroIdentificacion(),
                inscripcion.getNombres(),
                inscripcion.getApellidos(),
                inscripcion.getFechaNacimiento(),
                inscripcion.getEmail(),
                inscripcion.getTelefono(),
                inscripcion.getDireccion(),
                ciudadDto,
                inscripcion.getCiudadOtra(),
                marcaDto);
    }
}