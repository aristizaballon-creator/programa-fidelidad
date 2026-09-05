package com.fidelidad.programa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fidelidad.programa.entity.TipoIdentificacion;

public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Integer> {

    List<TipoIdentificacion> findAllByOrderByNombreAsc();
}