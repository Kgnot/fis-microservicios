package uni.fis.contenido.dto;
import lombok.Data;

@Data
public class CrearComentarioDTO {
    private String textoContenido;
    private Integer usuario;
    private Integer idPublicacion;
    private Integer idComentarioPadre;
}


