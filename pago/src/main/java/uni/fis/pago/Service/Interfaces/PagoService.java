package uni.fis.pago.Service.Interfaces;

import uni.fis.pago.Model.PagoDTO.*;

public interface PagoService {
    Integer doPago(PagoRequest pagoRequest);
    PagoResponse verDetallesPago(Integer idPago);
    
}
