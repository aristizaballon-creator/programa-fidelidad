package com.fidelidad.programa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fidelidad.programa.dto.InscripcionRequest;
import com.fidelidad.programa.dto.InscripcionResponse;
import com.fidelidad.programa.service.InscripcionServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionControlador {

    private final InscripcionServicio inscripcionServicio;

    public InscripcionControlador(InscripcionServicio inscripcionServicio) {
        this.inscripcionServicio = inscripcionServicio;
    }

    @PostMapping
    public ResponseEntity<InscripcionResponse> registrar(@Valid @RequestBody InscripcionRequest request) {
        InscripcionResponse response = inscripcionServicio.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<InscripcionResponse> listar() {
        return inscripcionServicio.listar();
    }
}