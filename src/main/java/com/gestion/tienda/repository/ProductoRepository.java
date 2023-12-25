package com.gestion.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
