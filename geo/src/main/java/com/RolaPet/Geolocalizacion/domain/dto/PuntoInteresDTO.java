package com.RolaPet.Geolocalizacion.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoInteresDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private String tipo;
    private String categoria;
    private String direccion;
    private String telefono;
    private String sitioWeb;
    private String imagenUrl;
    private Double calificacionPromedio;
    private Integer totalCalificaciones;
    private Double distancia;
    private Boolean activo;
}