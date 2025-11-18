package com.RolaPet.Geolocalizacion.domain.entity;

import com.RolaPet.Geolocalizacion.domain.enums.TipoUbicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ubicaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    private Double precision;  // Precisión en metros

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private Boolean compartida = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoUbicacion tipo;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}