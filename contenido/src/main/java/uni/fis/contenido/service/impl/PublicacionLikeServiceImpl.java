package uni.fis.contenido.service.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uni.fis.contenido.dto.LikeResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.exception.PublicacionNoEncontradaException;
import uni.fis.contenido.repository.PublicacionRepository;
import uni.fis.contenido.service.PublicacionLikeService;

@Service
public class PublicacionLikeServiceImpl implements PublicacionLikeService {

    private final PublicacionRepository publicacionRepository;
    private final SimpMessagingTemplate ws;

    public PublicacionLikeServiceImpl(
            PublicacionRepository publicacionRepository,
            SimpMessagingTemplate ws) {
        this.publicacionRepository = publicacionRepository;
        this.ws = ws;
    }

    @Override
    public LikeResponseDTO sumarLike(Integer idPublicacion) {

        PublicacionEntity pub = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new PublicacionNoEncontradaException(idPublicacion));

        pub.setLikes(pub.getLikes() + 1);
        publicacionRepository.save(pub);

        LikeResponseDTO response = new LikeResponseDTO(pub.getId(), pub.getLikes());

        ws.convertAndSend("/topic/publicacion/" + idPublicacion + "/likes", response);

        return response;
    }

    @Override
    public LikeResponseDTO restarLike(Integer idPublicacion) {

        PublicacionEntity pub = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new PublicacionNoEncontradaException(idPublicacion));

        pub.setLikes(Math.max(pub.getLikes() - 1, 0));
        publicacionRepository.save(pub);

        LikeResponseDTO response = new LikeResponseDTO(pub.getId(), pub.getLikes());

        ws.convertAndSend("/topic/publicacion/" + idPublicacion + "/likes", response);

        return response;
    }
}
