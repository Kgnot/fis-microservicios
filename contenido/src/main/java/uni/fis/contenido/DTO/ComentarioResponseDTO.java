package uni.fis.contenido.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioResponseDTO {
    private Long id;
    private Long usuario;
    private String textoContenido;
    private LocalDateTime fechaContenido;
    private Long idPublicacion;
    private Long idComentarioPadre;
}
