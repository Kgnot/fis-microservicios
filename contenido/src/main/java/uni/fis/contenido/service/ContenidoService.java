package uni.fis.contenido.service;

import java.util.List;
import uni.fis.contenido.entity.ContenidoEntity;

public interface ContenidoService {
    ContenidoEntity crearContenido(String texto, Integer usuario);
    List<ContenidoEntity> ordenarPorFecha();
    List<ContenidoEntity> listarPorUsuario(Integer usuario);
}
