package uni.fis.contenido.service.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uni.fis.contenido.dto.LikeResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.entity.PublicacionLikeEntity;
import uni.fis.contenido.exception.LikeDuplicadoException;
import uni.fis.contenido.exception.PublicacionNoEncontradaException;
import uni.fis.contenido.repository.PublicacionLikeRepository;
import uni.fis.contenido.repository.PublicacionRepository;
import uni.fis.contenido.service.PublicacionLikeService;

@Service
public class PublicacionLikeServiceImpl implements PublicacionLikeService {

    private final PublicacionLikeRepository likeRepository;
    private final PublicacionRepository publicacionRepository;
    private final SimpMessagingTemplate ws;

    public PublicacionLikeServiceImpl(
            PublicacionLikeRepository likeRepository,
            PublicacionRepository publicacionRepository,
            SimpMessagingTemplate ws
    ) {
        this.likeRepository = likeRepository;
        this.publicacionRepository = publicacionRepository;
        this.ws = ws;
    }

    @Override
    public LikeResponseDTO darLike(Integer idPublicacion, Integer idUsuario) {

        // Validar publicación
        PublicacionEntity pub = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new PublicacionNoEncontradaException(
                        "La publicación con ID " + idPublicacion + " no existe"
                ));

        // Validar like duplicado
        if (likeRepository.existsByPublicacionIdAndIdUsuario(idPublicacion, idUsuario)) {
            throw new LikeDuplicadoException(idPublicacion, idUsuario);
        }

        // Crear like
        PublicacionLikeEntity like = new PublicacionLikeEntity();
        like.setIdUsuario(idUsuario);
        like.setPublicacion(pub);
        likeRepository.save(like);

        // Recalcular likes
        int totalLikes = likeRepository.countByPublicacionId(idPublicacion);
        pub.setLikes(totalLikes);
        publicacionRepository.save(pub);

        enviarEventoWs(idPublicacion, totalLikes);

        return new LikeResponseDTO(idPublicacion, totalLikes);
    }

    @Override
    public LikeResponseDTO quitarLike(Integer idPublicacion, Integer idUsuario) {

        // Validar publicación
        PublicacionEntity pub = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new PublicacionNoEncontradaException(
                        "La publicación con ID " + idPublicacion + " no existe"
                ));

        // Validar que exista el like
        if (!likeRepository.existsByPublicacionIdAndIdUsuario(idPublicacion, idUsuario)) {
            throw new PublicacionNoEncontradaException(
                    "El usuario " + idUsuario + " no tiene like en la publicación " + idPublicacion
            );
        }

        // Eliminar like
        likeRepository.deleteByPublicacionIdAndIdUsuario(idPublicacion, idUsuario);

        // Recalcular
        int totalLikes = likeRepository.countByPublicacionId(idPublicacion);
        pub.setLikes(totalLikes);
        publicacionRepository.save(pub);

        enviarEventoWs(idPublicacion, totalLikes);

        return new LikeResponseDTO(idPublicacion, totalLikes);
    }

    private void enviarEventoWs(Integer idPublicacion, int totalLikes) {
        ws.convertAndSend("/topic/publicacion/" + idPublicacion + "/likes", totalLikes);
    }
}
