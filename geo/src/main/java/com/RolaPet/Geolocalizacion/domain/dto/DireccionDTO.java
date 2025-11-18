package com.RolaPet.Geolocalizacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
    private String direccionCompleta;
    private String calle;
    private String numero;
    private String ciudad;
    private String departamento;
    private String pais;
    private String codigoPostal;
    private Double latitud;
    private Double longitud;
}
