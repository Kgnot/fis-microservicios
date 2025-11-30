package uni.fis.contenido.service.impl;

import org.springframework.stereotype.Service;
import uni.fis.contenido.entity.ContenidoEntity;
import uni.fis.contenido.repository.ContenidoRepository;
import uni.fis.contenido.service.ContenidoService;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;


@Service
@Transactional
class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidoRepository;

    public ContenidoServiceImpl(ContenidoRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    @Override
    public ContenidoEntity crearContenido(String texto, Integer usuario) {
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
    public List<ContenidoEntity> listarPorUsuario(Integer usuario) {
        return contenidoRepository.findByIdAutor(usuario);
    }
}
