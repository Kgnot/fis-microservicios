package uni.fis.pago.Model.OrdenCompraDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrdenCompraRequest {
    private Integer id_pago;
        private BigDecimal monto_total;
}
