package com.ferremas.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant fecha = Instant.now();

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /* ---- getters & setters ---- */

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public Instant getFecha()                 { return fecha; }
    public void setFecha(Instant f)           { this.fecha = f; }

    public BigDecimal getTotal()              { return total; }
    public void setTotal(BigDecimal t)        { this.total = t; }

    public Cliente getCliente()               { return cliente; }
    public void setCliente(Cliente c)         { this.cliente = c; }
}
