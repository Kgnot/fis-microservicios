package uni.fis.pago.Service.Interfaces;

import java.util.List;

import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraRequest;
import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraResponse;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemResponse;

public interface OrdenCompraService {
    Integer crearOrdenCompra(OrdenCompraRequest OrdenCompraRequest);
    void eliminarOrdenCompra(Integer idOrdenCompra);
    OrdenCompraResponse consultarOrdenCompra(Integer idOrdenCompra);
    List<OrdenItemResponse> consultarOrdenesItems(Integer idOrdenCompra);
}
