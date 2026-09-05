package com.fidelidad.programa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fidelidad.programa.entity.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByTipoIdentificacionIdAndNumeroIdentificacionAndMarcaId(
            Integer tipoIdentificacionId, String numeroIdentificacion, Integer marcaId);
}