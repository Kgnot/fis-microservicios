package FIS.proyectoFinal.ProveedorService.Infraestructura;

import FIS.proyectoFinal.ProveedorService.Aplicacion.service.ProveedorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private ProveedorService proveedorService;

    @PostMapping("/api/proveedores/")
    public void crearProveedor(){

    }





}
