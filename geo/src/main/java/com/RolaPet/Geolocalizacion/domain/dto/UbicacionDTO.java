package com.RolaPet.Geolocalizacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    private Long id;
    private Long usuarioId;
    private Double latitud;
    private Double longitud;
    private Double precision;
    private LocalDateTime timestamp;
    private Boolean compartida;
    private String tipo;
}
