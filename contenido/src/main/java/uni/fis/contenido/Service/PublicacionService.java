package uni.fis.foro.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import uni.fis.contenido.DTO.CrearPublicacionDTO;
import uni.fis.contenido.Repository.PublicacionRepository;
import uni.fis.foro.Repository.ForoRepository;
import uni.fis.foro.Repository.MultimediaRepository;
import uni.fis.contenido.Service.ContenidoService;
import uni.fis.contenido.DTO.PublicacionResponseDTO;
import uni.fis.contenido.Entity.PublicacionEntity;
import uni.fis.contenido.Entity.ContenidoEntity;
import uni.fis.foro.Entity.ForoEntity;
import uni.fis.foro.Entity.MultimediaEntity;


public interface PublicacionService {
    PublicacionEntity crearPublicacion(CrearPublicacionDTO dto);
    List<PublicacionResponseDTO> listarPorForo(Long idForo);
    List<PublicacionResponseDTO> listarPorUsuario(String usuario);
}

@Service
class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final ContenidoService contenidoService;
    private final ForoRepository foroRepository;
    private final MultimediaRepository multimediaRepository;

    public PublicacionServiceImpl(
            PublicacionRepository publicacionRepository,
            ContenidoService contenidoService,
            ForoRepository foroRepository,
            MultimediaRepository multimediaRepository) {
        this.publicacionRepository = publicacionRepository;
        this.contenidoService = contenidoService;
        this.foroRepository = foroRepository;
        this.multimediaRepository = multimediaRepository;
    }

    @Override
    public PublicacionEntity crearPublicacion(CrearPublicacionDTO dto) {

        ContenidoEntity contenido = contenidoService.crearContenido(dto.getTextoContenido(), dto.getUsuario());
        ForoEntity foro = foroRepository.findById(dto.getIdForo()).orElseThrow();

        MultimediaEntity multimedia = null;
        if (dto.getIdMultimedia() != null) {
            multimedia = multimediaRepository.findById(dto.getIdMultimedia()).orElse(null);
        }

        PublicacionEntity pub = new PublicacionEntity();
        pub.setTitulo(dto.getTitulo());
        pub.setContenido(contenido);
        pub.setMultimedia(multimedia);
        pub.setForo(foro);
        pub.setLikes(0);

        return publicacionRepository.save(pub);
    }

    @Override
    public List<PublicacionResponseDTO> listarPorForo(Long idForo) {
        ForoEntity foro = foroRepository.findById(idForo).orElseThrow();
        return publicacionRepository.findByForo(foro).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<PublicacionResponseDTO> listarPorUsuario(String usuario) {
        return publicacionRepository.findAll().stream()
                .filter(p -> p.getContenido().getUsuario().equals(usuario))
                .map(this::mapToDTO)
                .toList();
    }

    private PublicacionResponseDTO mapToDTO(PublicacionEntity pub) {
        PublicacionResponseDTO dto = new PublicacionResponseDTO();

        dto.setId(pub.getId());
        dto.setTitulo(pub.getTitulo());
        dto.setUsuario(pub.getContenido().getUsuario());
        dto.setLikes(pub.getLikes());
        dto.setTextoContenido(pub.getContenido().getTexto());
        dto.setFechaContenido(pub.getContenido().getFecha());
        dto.setIdForo(pub.getForo().getId());
        dto.setNombreForo(pub.getForo().getNombre());

        dto.setUrlMultimedia(pub.getMultimedia() != null ? pub.getMultimedia().getUrl() : null);

        return dto;
    }
}

