package com.rolapet.Moderacion.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Este objeto se usa para RECIBIR la respuesta del microservicio
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeracionResponseDTO {
    private Boolean aprobado;         // ¿Se aprobó? true/false
    private String mensaje;           // Mensaje para el usuario
    private Integer numeroInfracciones; // Infracciones acumuladas
}
