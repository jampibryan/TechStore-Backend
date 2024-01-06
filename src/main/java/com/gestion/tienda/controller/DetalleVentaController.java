package com.gestion.tienda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.exception.ResourceNotFoundException;
import com.gestion.tienda.model.DetalleVenta;
import com.gestion.tienda.repository.DetalleVentaRepository;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1")

public class DetalleVentaController {
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @GetMapping("/detalleVentas")
    public List<DetalleVenta> listarProductos() {
        return detalleVentaRepository.findAll();
    }

    @GetMapping("/detalleVentas/{id}")
    public ResponseEntity<DetalleVenta> ListarDetalleVentaPorId(@PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de la Venta no encontrado para este id :: " + id));
        return ResponseEntity.ok().body(detalleVenta);
    }
}
