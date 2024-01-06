package com.gestion.tienda.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.gestion.tienda.model.Venta;
import com.gestion.tienda.model.Cliente;
import com.gestion.tienda.model.Producto;
import com.gestion.tienda.repository.VentaRepository;
import com.gestion.tienda.repository.ClienteRepository;
import com.gestion.tienda.repository.ProductoRepository;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1")

public class VentaController {
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/ventas")
    public List<Venta> listarProductos() {
        return ventaRepository.findAll();
    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> ListarVentaPorId(@PathVariable Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrado para este id :: " + id));
        return ResponseEntity.ok().body(venta);
    }

    @PostMapping("/ventas")
    public ResponseEntity<Venta> guardarVenta(@RequestBody Venta venta) {
        // Buscar el cliente por su ID
        Cliente cliente = clienteRepository.findById(venta.getCliente().getId()).get();
        // Asignar el cliente a la venta
        venta.setCliente(cliente);

        // Buscar el producto por su ID
        Producto producto = productoRepository.findById(venta.getProducto().getId()).get();
        // Asignar el producto a la venta
        venta.setProducto(producto);

        // Agregar el monto total de la venta
        int cantidadVenta = venta.getCantidad();
        float precioProducto = producto.getPrecio();
        float montoTotal = cantidadVenta * precioProducto;
        venta.setMontoTotal(montoTotal);

        // Agregar la fecha de la venta
        venta.setFecha(LocalDate.now());

        // Actualizar el stock del producto
        int stockActual = producto.getStock();

        if (stockActual >= cantidadVenta) {
            producto.setStock(stockActual - cantidadVenta);
            productoRepository.save(producto); // Guarda el producto con el stock actualizado

            // Guardar la venta
            ventaRepository.save(venta);

            return ResponseEntity.ok(venta);
        } else {
            // return null;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/ventas/{id}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Long id, @RequestBody Venta ventaRequest) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada para este id :: " + id));

        int cantidadVentaPrevia = venta.getCantidad(); // 4

        venta.setCantidad(ventaRequest.getCantidad());

        // Busca el cliente por su ID y asignala a la venta
        Cliente cliente = clienteRepository.findById(ventaRequest.getCliente().getId()).get();
        venta.setCliente(cliente);

        // Busca el producto por su ID y asignala a la venta
        Producto producto = productoRepository.findById(ventaRequest.getProducto().getId()).get();
        venta.setProducto(producto);

        // Agregar el monto total de la venta
        int cantidadVenta = venta.getCantidad();
        float precioProducto = producto.getPrecio();
        float montoTotal = cantidadVenta * precioProducto;
        venta.setMontoTotal(montoTotal);

        // Agregar la fecha de la venta
        venta.setFecha(LocalDate.now());

        // Lógica para obtener el stock original
        int stockActual = producto.getStock(); // 6
        int stockOriginal = cantidadVentaPrevia + stockActual; // 10

        int cantidadVentaActualizada = venta.getCantidad(); // 8

        // Actualiza el stock del producto
        if (stockOriginal >= cantidadVentaActualizada) {
            producto.setStock(stockOriginal - cantidadVentaActualizada);
            productoRepository.save(producto); // Guarda el producto con el stock actualizado

            Venta ventaActualizada = ventaRepository.save(venta);
            return ResponseEntity.ok(ventaActualizada);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarVenta(@PathVariable Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada para este id :: " + id));

        // Actualizar el stock del producto
        // Buscar el producto por su ID
        Producto producto = productoRepository.findById(venta.getProducto().getId()).get();
        int stockActual = producto.getStock();
        int cantidadVenta = venta.getCantidad();

        producto.setStock(stockActual + cantidadVenta);
        productoRepository.save(producto); // Guarda el producto con el stock actualizado

        // Elimina la venta
        ventaRepository.delete(venta);

        Map<String, Boolean> response = new HashMap<>();

        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
