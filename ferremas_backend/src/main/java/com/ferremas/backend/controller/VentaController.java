package com.ferremas.backend.controller;

import com.ferremas.backend.model.Venta;
import com.ferremas.backend.repository.VentaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaRepository repo;

    @GetMapping
    public List<Venta> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Venta> crear(@RequestBody Venta v) {
        return new ResponseEntity<>(repo.save(v), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Venta actualizar(@PathVariable Long id, @RequestBody Venta v) {
        v.setId(id);
        return repo.save(v);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
