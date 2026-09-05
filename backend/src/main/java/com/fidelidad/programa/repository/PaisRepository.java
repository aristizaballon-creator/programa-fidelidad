package com.fidelidad.programa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fidelidad.programa.entity.Pais;

public interface PaisRepository extends JpaRepository<Pais, Integer> {

    List<Pais> findAllByOrderByNombreAsc();
}