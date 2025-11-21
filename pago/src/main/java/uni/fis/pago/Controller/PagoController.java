package uni.fis.pago.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraRequest;
import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraResponse;
import uni.fis.pago.Model.PagoDTO.PagoRequest;
import uni.fis.pago.Model.PagoDTO.PagoResponse;
import uni.fis.pago.Service.Interfaces.OrdenCompraService;
import uni.fis.pago.Service.Interfaces.OrdenItemService;
import uni.fis.pago.Service.Interfaces.PagoService;

@RestController 
@RequestMapping("api/pago")
public class PagoController{
    @Autowired
    OrdenCompraService ordenCompraService;
    @Autowired
    OrdenItemService ordenItemService;
    @Autowired
    PagoService pagoService;
    @PostMapping("/crearPago")
    public ResponseEntity<Integer> crearEntityPago(@RequestBody PagoRequest pagoRequest) {
        Integer pagoId = pagoService.doPago(pagoRequest);
        return new ResponseEntity<>(pagoId, HttpStatus.CREATED);
    }
    @GetMapping("/ObtenerPago/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(@PathVariable Integer id){
        PagoResponse pago = pagoService.verDetallesPago(id);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }
    @PostMapping("/crearOrdenCompra")
    public ResponseEntity<Integer> crearEntityOrdenCompra(@RequestBody OrdenCompraRequest ordenCompraRequest){
        Integer ordenCompraId = ordenCompraService.crearOrdenCompra(ordenCompraRequest);
        return new ResponseEntity<>(ordenCompraId, HttpStatus.CREATED);
    }
    @GetMapping("/ObtenerOrdenCompra/{id}")
    public ResponseEntity<OrdenCompraResponse> obtenerOrdenCompraPorId(@PathVariable Integer id){
        OrdenCompraResponse response = ordenCompraService.consultarOrdenCompra(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}