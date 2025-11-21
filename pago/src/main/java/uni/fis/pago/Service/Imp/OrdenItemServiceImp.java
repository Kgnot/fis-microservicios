package uni.fis.pago.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Exceptions.Exceptions;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemResponse;
import uni.fis.pago.Repository.OrdenItemRepository;
import uni.fis.pago.Service.Interfaces.OrdenItemService;

@Service
public class OrdenItemServiceImp implements OrdenItemService{
    @Autowired 
    OrdenItemRepository ordenItemRepository;
    @Override
    public Integer agregarOrdenItem(OrdenItemRequest ordenItemRequest){
        OrdenItem ordenItem = OrdenItem.builder()
                            .idItem(ordenItemRequest.getId_item())
                            .cantidad(ordenItemRequest.getCantidad())
                            .valor_unitario(ordenItemRequest.getValor_unitario())
                            .subtotal(ordenItemRequest.getSubtotal())
                            .idOrdenCompra(ordenItemRequest.getId_orden_compra())
                            .build();
        ordenItemRepository.save(ordenItem);
        return ordenItem.getId();
    }
    @Override
    public void eliminarOrdenItem(Integer id){
        if(!ordenItemRepository.existsById(id)){
            throw new Exceptions("Orden de item con el id "+ id +" no fue encontrado", "ORDEN_ITEM_NOT_FOUND" );
        }
        ordenItemRepository.deleteById(id);
    }
    @Override
    public OrdenItemResponse consultarOrdenItem(Integer idOrdenItem){
        OrdenItem ordenItem = ordenItemRepository.findById(idOrdenItem).
        orElseThrow(() -> new Exceptions("Orden de item con el id "+ idOrdenItem +" no fue encontrado", "ORDEN_ITEM_NOT_FOUND"));
        OrdenItemResponse ordenItemResponse = OrdenItemResponse.builder()
                                              .id(ordenItem.getId())
                                              .id_item(ordenItem.getIdItem())
                                              .cantidad(ordenItem.getCantidad())
                                              .valor_unitario(ordenItem.getValor_unitario())
                                              .id_orden_compra(ordenItem.getIdOrdenCompra())
                                              .subtotal(ordenItem.getSubtotal())
                                              .build();
        return ordenItemResponse;
    }

}
