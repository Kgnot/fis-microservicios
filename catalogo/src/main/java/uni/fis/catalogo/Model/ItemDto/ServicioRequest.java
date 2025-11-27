package uni.fis.catalogo.Model.ItemDto;

import java.math.BigDecimal;
import java.time.LocalTime;
import org.postgresql.util.PGInterval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ServicioRequest{
    private String nombre;
    private BigDecimal precio;
    private PGInterval duracion;
    private LocalTime horario;
}