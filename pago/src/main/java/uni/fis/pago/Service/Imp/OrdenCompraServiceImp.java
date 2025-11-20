package uni.fis.pago.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.fis.pago.Entity.OrdenCompra;
import uni.fis.pago.Model.OrdenCompraDTO.*;
import uni.fis.pago.Repository.OrdenCompraRepository;
import uni.fis.pago.Exceptions.Exceptions;
import uni.fis.pago.Service.Interfaces.OrdenCompraService;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemResponse;
import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Repository.OrdenItemRepository;

import java.util.List;
import java.util.ArrayList;

public class OrdenCompraServiceImp implements OrdenCompraService{
    @Autowired
    OrdenCompraRepository ordenCompraRepository;
    @Autowired
    OrdenItemRepository ordenItemRepository;

    @Override
    public Integer crearOrdenCompra(OrdenCompraRequest ordenCompraRequest){
        OrdenCompra ordenCompra = OrdenCompra.builder()
                                    .fecha(ordenCompraRequest.getFecha())
                                    .total(ordenCompraRequest.getTotal())
                                    .id_pago(ordenCompraRequest.getId_pago())
                                .build();
        return ordenCompra.getId();
    }
    @Override
    public void eliminarOrdenCompra(Integer idOrdenCompra){
        if(!ordenCompraRepository.existsById(idOrdenCompra)){
            throw new Exceptions("La Orden de compra con el id " + idOrdenCompra +" no existe","ORDEN_COMPRA_NOT_FOUND");
        }
        ordenCompraRepository.deleteById(idOrdenCompra);
    }
    @Override
    public OrdenCompraResponse consultarOrdenCompra(Integer idOrdenCompra){
        OrdenCompra ordenCompra = ordenCompraRepository.findById(idOrdenCompra)
                            .orElseThrow(()-> new Exceptions("La Orden de compra con el id " + idOrdenCompra +" no existe","ORDEN_COMPRA_NOT_FOUND"));
        OrdenCompraResponse response = OrdenCompraResponse.builder()
                                        .id(ordenCompra.getId())
                                        .fecha(ordenCompra.getFecha())
                                        .total(ordenCompra.getTotal())
                                        .id_pago(ordenCompra.getId_pago())
                                    .build();                    
        return response;
    }
    @Override
    public List<OrdenItemResponse> consultarOrdenesItems(Integer idOrdenCompra){
        List<OrdenItem> ordenesItems = ordenItemRepository.findById_orden_compra(idOrdenCompra);
        List<OrdenItemResponse> response = new ArrayList<>();
        for(OrdenItem ordenItem : ordenesItems){
            OrdenItemResponse ordenItemResponse = OrdenItemResponse.builder()
                                            .id(ordenItem.getId())
                                            .id_item(ordenItem.getId_item())
                                            .cantidad(ordenItem.getCantidad())
                                            .valor_unitario(ordenItem.getValor_unitario())
                                            .id_orden_compra(ordenItem.getId_orden_compra())
                                            .subtotal(ordenItem.getSubtotal())
                                            .build();
            response.add(ordenItemResponse);
        }
        return response;
    }
}
