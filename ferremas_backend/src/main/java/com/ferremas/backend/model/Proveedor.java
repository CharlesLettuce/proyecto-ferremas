package com.ferremas.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String contacto;
    private String email;
    private String telefono;

    /* ---- getters & setters ---- */

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getNombre()              { return nombre; }
    public void setNombre(String n)        { this.nombre = n; }

    public String getContacto()            { return contacto; }
    public void setContacto(String c)      { this.contacto = c; }

    public String getEmail()               { return email; }
    public void setEmail(String e)         { this.email = e; }

    public String getTelefono()            { return telefono; }
    public void setTelefono(String t)      { this.telefono = t; }
}
