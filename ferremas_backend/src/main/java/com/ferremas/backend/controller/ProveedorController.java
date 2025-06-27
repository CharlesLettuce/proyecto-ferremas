package com.ferremas.backend.controller;

import com.ferremas.backend.model.Proveedor;
import com.ferremas.backend.repository.ProveedorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository repo;

    @GetMapping
    public List<Proveedor> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor p) {
        return new ResponseEntity<>(repo.save(p), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable Long id, @RequestBody Proveedor p) {
        p.setId(id);
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
