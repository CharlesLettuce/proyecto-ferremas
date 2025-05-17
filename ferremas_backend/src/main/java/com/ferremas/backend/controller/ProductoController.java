package com.ferremas.backend.controller;

import com.ferremas.backend.model.Producto;
import com.ferremas.backend.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(
        @RequestBody Producto producto
    ) {
        try {
            Producto nuevoProducto = productoRepository.save(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(
        @PathVariable("id") Integer id
    ) {
        Optional<Producto> productoData = productoRepository.findById(id);

        return productoData
            .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
        @PathVariable("id") Integer id,
        @RequestBody Producto productoDetalles
    ) {
        Optional<Producto> productoData = productoRepository.findById(id);

        if (productoData.isPresent()) {
            Producto productoExistente = productoData.get();
            productoExistente.setCodigoSku(productoDetalles.getCodigoSku());
            productoExistente.setNombre(productoDetalles.getNombre());
            productoExistente.setDescripcion(productoDetalles.getDescripcion());
            productoExistente.setPrecioUnitario(
                productoDetalles.getPrecioUnitario()
            );
            productoExistente.setStock(productoDetalles.getStock());
            productoExistente.setCategoria(productoDetalles.getCategoria());
            return new ResponseEntity<>(
                productoRepository.save(productoExistente),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> eliminarProducto(
        @PathVariable("id") Integer id
    ) {
        try {
            if (!productoRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            productoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
