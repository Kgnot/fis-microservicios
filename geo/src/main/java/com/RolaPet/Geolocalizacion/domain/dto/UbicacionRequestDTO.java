package com.RolaPet.Geolocalizacion.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UbicacionRequestDTO {
    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitud;

    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitud;

    private Double precision;
}
