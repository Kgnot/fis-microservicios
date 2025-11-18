package com.RolaPet.Geolocalizacion.domain.entity;

import com.RolaPet.Geolocalizacion.domain.enums.CategoriaPunto;
import com.RolaPet.Geolocalizacion.domain.enums.TipoPuntoInteres;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "puntos_interes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // OPCIÓN 1: Con PostGIS (Recomendado)
    @Column(columnDefinition = "geometry(Point,4326)", name = "ubicacion")
    private Point ubicacion;  // Requiere dependencia org.locationtech:jts-core

    // OPCIÓN 2: Sin PostGIS (Alternativa simple)
    private Double latitud;
    private Double longitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoPuntoInteres tipo;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CategoriaPunto categoria;

    @Column(length = 300)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(name = "sitio_web", length = 200)
    private String sitioWeb;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "calificacion_promedio", columnDefinition = "NUMERIC(3,2)")
    private Double calificacionPromedio = 0.0;

    @Column(name = "total_calificaciones")
    private Integer totalCalificaciones = 0;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @Column(name = "administrador_id")
    private Long administradorId;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
