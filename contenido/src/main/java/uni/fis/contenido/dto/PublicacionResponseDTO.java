package uni.fis.contenido.dto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PublicacionResponseDTO {
    private Integer id;
    private String titulo;
    private Integer usuario;
    private Integer likes;
    private String textoContenido;
    private LocalDateTime fechaContenido;
    private Integer idForo;
    private Integer idMultimedia;
}
