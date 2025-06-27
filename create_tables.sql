-- Script inicial para Ferremas
CREATE DATABASE IF NOT EXISTS ferremas_db;
USE ferremas_db;

CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_sku     VARCHAR(100) UNIQUE NOT NULL,
    nombre         VARCHAR(255) NOT NULL,
    descripcion    TEXT,
    precio_unitario DECIMAL(10,2) NOT NULL,
    stock          INT NOT NULL,
    categoria      VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(255) NOT NULL,
    email    VARCHAR(255),
    telefono VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS proveedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(255) NOT NULL,
    contacto  VARCHAR(255),
    email     VARCHAR(255),
    telefono  VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ventas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha   TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    total   DECIMAL(12,2)   NOT NULL,
    cliente_id BIGINT,
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (cliente_id)
        REFERENCES clientes(id) ON DELETE SET NULL
);
