package com.ferremas.backend.controller;

import com.ferremas.backend.model.Cliente;
import com.ferremas.backend.repository.ClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repo;

    @GetMapping
    public List<Cliente> listar() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente c) {
        return new ResponseEntity<>(repo.save(c), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente c) {
        c.setId(id);
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
