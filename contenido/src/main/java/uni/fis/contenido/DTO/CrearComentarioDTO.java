package uni.fis.contenido.dto;
import lombok.Data;

@Data
public class CrearComentarioDTO {
    private String textoContenido;
    private Long usuario;
    private Long idPublicacion;
    private Long idComentarioPadre;
}


