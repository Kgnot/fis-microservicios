package com.rolapet.Moderacion.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data

@NoArgsConstructor
@AllArgsConstructor
public class ModeracionRequestDTO {

    private Long usuarioId;
    private String contenido;
    private String tipoContenido;
}

