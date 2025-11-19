package uni.fis.catalogo.Model.ItemDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

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
    private Duration duracion;
    private LocalTime horario;
}