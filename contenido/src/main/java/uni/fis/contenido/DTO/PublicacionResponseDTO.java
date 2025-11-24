package uni.fis.contenido.dto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PublicacionResponseDTO {
    private Long id;
    private String titulo;
    private String usuario;
    private Integer likes;
    private String textoContenido;
    private LocalDateTime fechaContenido;
    private Long idForo;
    private String nombreForo;
    private String urlMultimedia;
}
