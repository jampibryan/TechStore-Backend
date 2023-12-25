package com.gestion.tienda.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.exception.ResourceNotFoundException;
import com.gestion.tienda.model.Categoria;
import com.gestion.tienda.repository.CategoriaRepository;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/categorias")
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @PostMapping("/categorias")
    public Categoria guardarCategoria(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> ListarCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrado para este id :: " + id));
        return ResponseEntity.ok().body(categoria);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id,
            @RequestBody Categoria categoriaRequest) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrado para este id :: " + id));
                
        categoria.setDescripcion(categoriaRequest.getDescripcion());

        Categoria categoriaActualizado = categoriaRepository.save(categoria);
        return ResponseEntity.ok(categoriaActualizado);
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarCategoria(@PathVariable Long id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrado para este id :: " + id));

        categoriaRepository.delete(categoria);

        Map<String, Boolean> response = new HashMap<>();

        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
