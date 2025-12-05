package uni.fis.contenido.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDTO {
    private LocalDateTime fecha;
    private int codigo;
    private String mensaje;
}
