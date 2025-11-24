package uni.fis.contenido.service;   

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.contenido.dto.CrearComentarioDTO;
import uni.fis.contenido.dto.ComentarioResponseDTO;
import uni.fis.contenido.entity.ComentarioEntity;
import uni.fis.contenido.entity.ContenidoEntity;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.repository.ComentarioRepository;
import uni.fis.contenido.repository.PublicacionRepository;
import uni.fis.contenido.service.ContenidoService;
import java.util.List;



public interface ComentarioService {
    ComentarioEntity crearComentario(CrearComentarioDTO dto);
    List<ComentarioResponseDTO> listarPorUsuario(String usuario);
    List<ComentarioResponseDTO> listarPorPublicacion(Long idPublicacion);
}



@Service
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final ContenidoService contenidoService;

    public ComentarioServiceImpl(
            ComentarioRepository comentarioRepository,
            PublicacionRepository publicacionRepository,
            ContenidoService contenidoService) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.contenidoService = contenidoService;
        ContenidoEntity contenido = contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());

        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setContenido(contenido);

        ContenidoEntity contenido = contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());

        ComentarioEntity comentario = new Comentario();
        comentario.setContenido(contenido);

        if (dto.getIdComentarioPadre() != null) {
            comentario.setComentarioPadre(
                    comentarioRepository.findById(dto.getIdComentarioPadre()).orElseThrow()
            );
        }

        if (dto.getIdPublicacion() != null) {
            comentario.setPublicacion(
                    publicacionRepository.findById(dto.getIdPublicacion()).orElseThrow()
            );
        }

        return comentarioRepository.save(comentario);
    }

    @Override
    public List<ComentarioResponseDTO> listarPorUsuario(String usuario) {
        return comentarioRepository.findAll().stream()
                .filter(c -> c.getContenido().getUsuario().equals(usuario))
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ComentarioResponseDTO> listarPorPublicacion(Long idPublicacion) {
        PublicacionEntity pub = publicacionRepository.findById(idPublicacion).orElseThrow();

        return comentarioRepository.findByPublicacion(pub).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ComentarioResponseDTO mapToDTO(ComentarioEntity c) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setId(c.getId());
        dto.setUsuario(c.getContenido().getUsuario());
        dto.setTextoContenido(c.getContenido().getTexto());
        dto.setFechaContenido(c.getContenido().getFecha());
        dto.setIdPublicacion(c.getPublicacion() != null ? c.getPublicacion().getId() : null);
        dto.setIdComentarioPadre(c.getComentarioPadre() != null ? c.getComentarioPadre().getId() : null);
        return dto;
    }

    public ComentarioEntity crearComentario(CrearComentarioDTO dto) {
        ContenidoEntity contenido = contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setContenido(contenido);

        if (dto.getIdComentarioPadre() != null) {
            comentario.setComentarioPadre(
                    comentarioRepository.findById(dto.getIdComentarioPadre()).orElseThrow()
            );
        }

        if (dto.getIdPublicacion() != null) {
            comentario.setPublicacion(
                    publicacionRepository.findById(dto.getIdPublicacion()).orElseThrow()
            );
        }

        return comentarioRepository.save(comentario);
    }
}
