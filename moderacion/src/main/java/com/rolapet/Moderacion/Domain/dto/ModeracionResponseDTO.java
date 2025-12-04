package com.rolapet.Moderacion.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeracionResponseDTO {
    private Boolean aprobado;
    private String mensaje;
    private Integer numeroInfracciones;
}

