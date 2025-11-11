package FIS.proyectoFinal.ProveedorService.Aplicacion.service;

import FIS.proyectoFinal.ProveedorService.Aplicacion.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {

    private ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository){
        this.proveedorRepository = proveedorRepository;
    }



}
