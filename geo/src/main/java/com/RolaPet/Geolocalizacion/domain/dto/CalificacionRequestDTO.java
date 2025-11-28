package com.RolaPet.Geolocalizacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalificacionRequestDTO {
    private Long puntoInteresId;
    private Double calificacion;
    private String comentario;
}



