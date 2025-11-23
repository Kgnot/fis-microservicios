package uni.fis.pago.Model.OrdenCompraDTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrdenCompraRequest {
    private Date fecha;
    private Integer id_pago;
}
