package com.fidelidad.programa.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fidelidad.programa.dto.CiudadDto;
import com.fidelidad.programa.dto.DepartamentoDto;
import com.fidelidad.programa.dto.MarcaDto;
import com.fidelidad.programa.dto.PaisDto;
import com.fidelidad.programa.dto.TipoIdentificacionDto;
import com.fidelidad.programa.service.CatalogoServicio;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoControlador {

    private final CatalogoServicio catalogoServicio;

    public CatalogoControlador(CatalogoServicio catalogoServicio) {
        this.catalogoServicio = catalogoServicio;
    }

    @GetMapping("/tipos-identificacion")
    public List<TipoIdentificacionDto> tiposIdentificacion() {
        return catalogoServicio.listarTiposIdentificacion();
    }

    @GetMapping("/paises")
    public List<PaisDto> paises() {
        return catalogoServicio.listarPaises();
    }

    @GetMapping("/departamentos")
    public List<DepartamentoDto> departamentos(@RequestParam Integer paisId) {
        return catalogoServicio.listarDepartamentos(paisId);
    }

    @GetMapping("/ciudades")
    public List<CiudadDto> ciudades(@RequestParam Integer departamentoId) {
        return catalogoServicio.listarCiudades(departamentoId);
    }

    @GetMapping("/marcas")
    public List<MarcaDto> marcas() {
        return catalogoServicio.listarMarcas();
    }
}