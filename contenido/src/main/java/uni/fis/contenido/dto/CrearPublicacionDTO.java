package uni.fis.contenido.dto;

import lombok.Data;

@Data
public class CrearPublicacionDTO {
    private String titulo;
    private String textoContenido;
    private Integer usuario;
    private Integer idForo;
    private Integer idMultimedia; // opcional
}


