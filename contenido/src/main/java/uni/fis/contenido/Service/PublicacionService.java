package uni.fis.contenido.service;

import org.springframework.stereotype.Service;
import java.util.List;
import uni.fis.contenido.dto.CrearPublicacionDTO;
import uni.fis.contenido.repository.PublicacionRepository;
import uni.fis.contenido.dto.PublicacionResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.entity.ContenidoEntity;


public interface PublicacionService {
    PublicacionEntity crearPublicacion(CrearPublicacionDTO dto);
    List<PublicacionResponseDTO> listarPorForo(Long idForo);
    List<PublicacionResponseDTO> listarPorUsuario(Long usuario);
}

@Service
class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final ContenidoService contenidoService;

    public PublicacionServiceImpl(
            PublicacionRepository publicacionRepository,
            ContenidoService contenidoService,
            Long foroRepository,
            Long multimediaRepository) {
        this.publicacionRepository = publicacionRepository;
        this.contenidoService = contenidoService;
    }

    @Override
    public PublicacionEntity crearPublicacion(CrearPublicacionDTO dto) {

        ContenidoEntity contenido = contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());

        PublicacionEntity pub = new PublicacionEntity();
        pub.setTitulo(dto.getTitulo());
        pub.setContenido(contenido);
        pub.setMultimedia(dto.getIdMultimedia());
        pub.setForo(dto.getIdForo());
        pub.setLikes(0);

        return publicacionRepository.save(pub);
    }

    @Override
    public List<PublicacionResponseDTO> listarPorForo(Long idForo) {
        return publicacionRepository.findByForo(idForo).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<PublicacionResponseDTO> listarPorUsuario(Long usuario) {
        return publicacionRepository.findAll().stream()
                .filter(p -> p.getContenido().getIdAutor().equals(usuario))
                .map(this::mapToDTO)
                .toList();
    }

    private PublicacionResponseDTO mapToDTO(PublicacionEntity pub) {
        PublicacionResponseDTO dto = new PublicacionResponseDTO();

        dto.setId(pub.getId());
        dto.setTitulo(pub.getTitulo());
        dto.setUsuario(pub.getContenido().getIdAutor());
        dto.setLikes(pub.getLikes());
        dto.setTextoContenido(pub.getContenido().getTexto());
        dto.setFechaContenido(pub.getContenido().getFechaCreacion());
        dto.setIdForo(pub.getForo());
        dto.setIdMultimedia(pub.getMultimedia());

        return dto;
    }
}

