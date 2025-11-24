package uni.fis.contenido.dto;
import lombok.Data;

@Data
public class CrearComentarioDTO {
    private String textoContenido;
    private String usuario;
    private Long idPublicacion;
    private Long idComentarioPadre;
}


