package com.fidelidad.programa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fidelidad.programa.entity.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    List<Ciudad> findByDepartamentoIdOrderByNombreAsc(Integer departamentoId);
}