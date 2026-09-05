package com.fidelidad.programa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fidelidad.programa.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    List<Marca> findByActivaTrueOrderByNombreAsc();
}