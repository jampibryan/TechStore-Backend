package com.gestion.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

}
