package com.rolapet.Moderacion.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Este objeto se usa para ENVIAR datos al microservicio
@Data

@NoArgsConstructor
@AllArgsConstructor
public class ModeracionRequestDTO {

    private Long usuarioId;      // ¿Quién envía el contenido?
    private String contenido;     // Texto a validar
    private String tipoContenido; // "COMENTARIO" o "PUBLICACION"
}

