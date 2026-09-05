package com.fidelidad.programa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelidad.programa.dto.CiudadDto;
import com.fidelidad.programa.dto.DepartamentoDto;
import com.fidelidad.programa.dto.MarcaDto;
import com.fidelidad.programa.dto.PaisDto;
import com.fidelidad.programa.dto.TipoIdentificacionDto;
import com.fidelidad.programa.repository.CiudadRepository;
import com.fidelidad.programa.repository.DepartamentoRepository;
import com.fidelidad.programa.repository.MarcaRepository;
import com.fidelidad.programa.repository.PaisRepository;
import com.fidelidad.programa.repository.TipoIdentificacionRepository;

@Service
@Transactional(readOnly = true)
public class CatalogoServicio {

    private final TipoIdentificacionRepository tipoIdentificacionRepository;
    private final PaisRepository paisRepository;
    private final DepartamentoRepository departamentoRepository;
    private final CiudadRepository ciudadRepository;
    private final MarcaRepository marcaRepository;

    public CatalogoServicio(
            TipoIdentificacionRepository tipoIdentificacionRepository,
            PaisRepository paisRepository,
            DepartamentoRepository departamentoRepository,
            CiudadRepository ciudadRepository,
            MarcaRepository marcaRepository) {
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.paisRepository = paisRepository;
        this.departamentoRepository = departamentoRepository;
        this.ciudadRepository = ciudadRepository;
        this.marcaRepository = marcaRepository;
    }

    public List<TipoIdentificacionDto> listarTiposIdentificacion() {
        return tipoIdentificacionRepository.findAllByOrderByNombreAsc().stream()
                .map(t -> new TipoIdentificacionDto(t.getId(), t.getCodigo(), t.getNombre()))
                .toList();
    }

    public List<PaisDto> listarPaises() {
        return paisRepository.findAllByOrderByNombreAsc().stream()
                .map(p -> new PaisDto(p.getId(), p.getNombre(), p.getCodigoIso2()))
                .toList();
    }

    public List<DepartamentoDto> listarDepartamentos(Integer paisId) {
        return departamentoRepository.findByPaisIdOrderByNombreAsc(paisId).stream()
                .map(d -> new DepartamentoDto(d.getId(), d.getNombre(), d.getPais().getId()))
                .toList();
    }

    public List<CiudadDto> listarCiudades(Integer departamentoId) {
        return ciudadRepository.findByDepartamentoIdOrderByNombreAsc(departamentoId).stream()
                .map(c -> new CiudadDto(c.getId(), c.getNombre(), c.getDepartamento().getId()))
                .toList();
    }

    public List<MarcaDto> listarMarcas() {
        return marcaRepository.findByActivaTrueOrderByNombreAsc().stream()
                .map(m -> new MarcaDto(m.getId(), m.getNombre()))
                .toList();
    }
}