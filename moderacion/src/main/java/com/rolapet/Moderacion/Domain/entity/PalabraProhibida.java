package com.rolapet.Moderacion.Domain.entity;

import jakarta.persistence.*;
import lombok.Data;

    @Entity  // 👈 Esto dice: "Soy una tabla en la base de datos"
    @Table(name = "palabras_prohibidas")  // 👈 Nombre de la tabla
    @Data  // 👈 Lombok crea automáticamente getters, setters, toString, etc.
    public class PalabraProhibida {

        @Id  // 👈 Esta es la LLAVE PRIMARIA (ID único)
        @GeneratedValue(strategy = GenerationType.IDENTITY)  // 👈 Se genera automáticamente (1, 2, 3...)
        private Integer id;

        @Column(nullable = false)  // 👈 Esta columna NO puede estar vacía
        private String palabra;  // La palabra prohibida (ej: "grosería")

        private String descripcion;  // Por qué está prohibida

        @Column(nullable = false)
        private Boolean activa;  // ¿Está activa? true/false


    }


