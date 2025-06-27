package com.ferremas.backend.repository;

import com.ferremas.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
