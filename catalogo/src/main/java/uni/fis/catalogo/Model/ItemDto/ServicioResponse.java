package uni.fis.catalogo.Model.ItemDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ServicioResponse {
    private Integer id;
    private Integer idCatalogo;
    private String nombre;
    private BigDecimal precio;
    private Date fechaCreacion;
    private BigDecimal valoración;
    private boolean disponible;
    private Duration duracion;
    private LocalTime horario;
}
