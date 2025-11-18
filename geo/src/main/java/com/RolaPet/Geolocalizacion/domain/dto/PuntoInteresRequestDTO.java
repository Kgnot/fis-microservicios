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
public class PuntoInteresRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    private String nombre;
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    @NotNull(message = "La latitud es obligatoria")
    @Min(value = -90, message = "Latitud mínima: -90")
    @Max(value = 90, message = "Latitud máxima: 90")
    private Double latitud;
    @NotNull(message = "La longitud es obligatoria")
    @Min(value = -180, message = "Longitud mínima: -180")
    @Max(value = 180, message = "Longitud máxima: 180")
    private Double longitud;
    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(
            regexp = "PARQUEADERO|TALLER|LAVADERO|VENTA_PARTES|ESTACION_CARGA|PUNTO_ENCUENTRO|EVENTO",
            message = "Tipo inválido. Valores permitidos: PARQUEADERO, TALLER, LAVADERO, VENTA_PARTES, ESTACION_CARGA, PUNTO_ENCUENTRO, EVENTO"
    )
    private String tipo;
    @Pattern(
            regexp = "SERVICIO|COMERCIO|EVENTO|OTRO",
            message = "Categoría inválida. Valores permitidos: SERVICIO, COMERCIO, EVENTO, OTRO"
    )
    private String categoria;
    @Size(max = 300, message = "La dirección no puede exceder 300 caracteres")
    private String direccion;
    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "Teléfono inválido. Debe tener entre 7 y 15 dígitos"
    )
    private String telefono;
    @Pattern(
            regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(/.*)?$",
            message = "URL inválida"
    )
    @Size(max = 200, message = "La URL no puede exceder 200 caracteres")
    private String sitioWeb;
    @Pattern(
            regexp = "^(https?://).*\\.(jpg|jpeg|png|gif|webp)$",
            message = "URL de imagen inválida. Debe ser JPG, PNG, GIF o WEBP"
    )
    @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    private String imagenUrl;
}
