package com.fidelidad.programa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fidelidad.programa.entity.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

    List<Departamento> findByPaisIdOrderByNombreAsc(Integer paisId);
}