package uni.fis.contenido.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioResponseDTO {
    private Integer id;
    private Integer usuario;
    private String textoContenido;
    private LocalDateTime fechaContenido;
    private Integer idPublicacion;
    private Integer idComentarioPadre;
}
