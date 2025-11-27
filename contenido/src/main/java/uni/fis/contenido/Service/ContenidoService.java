package uni.fis.contenido.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Comparator;
import uni.fis.contenido.entity.ContenidoEntity;
import uni.fis.contenido.repository.ContenidoRepository;

public interface ContenidoService {
    ContenidoEntity crearContenido(String texto, Long usuario);
    List<ContenidoEntity> ordenarPorFecha();
    List<ContenidoEntity> listarPorUsuario(Long usuario);
}

@Service
@Transactional
class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidoRepository;

    public ContenidoServiceImpl(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    @Override
    public ContenidoEntity crearContenido(String texto, Long usuario) {
        ContenidoEntity c = new ContenidoEntity();
        c.setTexto(texto);
        c.setIdAutor(usuario);
        c.setFechaCreacion(LocalDateTime.now());
        return contenidoRepository.save(c);
    }

    @Override
    public List<ContenidoEntity> ordenarPorFecha() {
        return contenidoRepository.findAll().stream()
                .sorted(Comparator.comparing(ContenidoEntity::getFechaCreacion).reversed())
                .toList();
    }

    @Override
    public List<ContenidoEntity> listarPorUsuario(Long usuario) {
        return contenidoRepository.findByUsuario(usuario);
    }
}
