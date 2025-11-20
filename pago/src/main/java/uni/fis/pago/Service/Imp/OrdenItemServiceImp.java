package uni.fis.pago.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Model.OrdenItemDTO.*;
import uni.fis.pago.Repository.OrdenItemRepository;
import uni.fis.pago.Service.Interfaces.OrdenItemService;

@Service
public class OrdenItemServiceImp implements OrdenItemService{
    @Autowired 
    OrdenItemRepository ordenItemRepository;
    @Override
    public Integer agregarOrdenItem(OrdenItemRequest ordenItemRequest){
        OrdenItem ordenItem = OrdenItem.builder()
                            .id_item(ordenItemRequest.getId_item())
                            .cantidad(ordenItemRequest.getCantidad())
                            .valor_unitario(ordenItemRequest.getValor_unitario())
                            .subtotal(ordenItemRequest.getSubtotal())
                            .id_orden_compra(ordenItemRequest.getId_orden_compra())
                            .build();
        ordenItemRepository.save(ordenItem);
        return ordenItem.getId();
    }
    @Override
    public void eliminarOrdenItem(Integer id){
        if(!ordenItemRepository.existsById(id)){
            throw new
        }
        ordenItemRepository.deleteById(id);
    }

}
