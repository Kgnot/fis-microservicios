package FIS.proyectoFinal.ProveedorService.Aplicacion.repository;

@org.springframework.stereotype.Repository
public interface Repository{

    void save();
    void findById(long id);
    void findByProduct(long pid);

}
