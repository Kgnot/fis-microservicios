package uni.fis.pago.Model.PagoDTO;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class PagoRequest {
    private Date fecha;
    private BigDecimal monto_total;
    private Integer id_usuario;
    private Integer id_metodo_pago;
}
