package uni.fis.pago.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uni.fis.pago.Entity.Pago;
import uni.fis.pago.Exceptions.Exceptions;
import uni.fis.pago.Model.PagoDTO.PagoRequest;
import uni.fis.pago.Model.PagoDTO.PagoResponse;
import uni.fis.pago.Repository.PagoRepository;
import uni.fis.pago.Service.Interfaces.PagoService;



@Service
@Log4j2
public class PagoServiceImp implements PagoService{
    @Autowired
    PagoRepository pagoRepository;
    @Override
    public Integer doPago(PagoRequest pagoRequest){
        log.info("Guardando información del pago: {}" + pagoRequest);

        Pago pago = Pago.builder()
                    .id(pagoRequest.getId())
                    .fecha(pagoRequest.getFecha())
                    .monto_total(pagoRequest.getMonto_total())
                    .idUsuario(pagoRequest.getId_usuario())
                    .idMetodoPago(pagoRequest.getId_metodo_pago())
                    .build();
        log.info("pago con el id "+pago.getId()+" ha sido procesado");
        pagoRepository.save(pago);
        return pago.getId();
    }
    @Override
    public PagoResponse verDetallesPago(Integer idPago){
        Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(()-> new Exceptions("El pago con id " + idPago + "no fue encontrado", "PAGO_NOT_FOUND"));
        PagoResponse response = PagoResponse.builder()
                                .id(pago.getId())
                                .fecha(pago.getFecha())
                                .monto_total(pago.getMonto_total())
                                .id_usuario(pago.getIdUsuario())
                                .id_metodo_pago(pago.getIdMetodoPago())
                                .build();
        return response;
    }
}
