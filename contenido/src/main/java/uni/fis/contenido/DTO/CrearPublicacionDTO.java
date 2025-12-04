package uni.fis.contenido.dto;

import lombok.Data;

@Data
public class CrearPublicacionDTO {
    private String titulo;
    private String textoContenido;
    private Long usuario;
    private Long idForo;
    private Long idMultimedia; // opcional
}


