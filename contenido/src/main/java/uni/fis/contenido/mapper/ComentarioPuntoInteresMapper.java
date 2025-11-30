package uni.fis.contenido.mapper;

import org.springframework.stereotype.Component;
import uni.fis.contenido.dto.ComentarioPuntoInteresDTO;
import uni.fis.contenido.entity.ComentarioEntity;
import uni.fis.contenido.entity.ComentarioPuntoInteresEntity;

@Component
public class ComentarioPuntoInteresMapper {

    public ComentarioPuntoInteresDTO toDTO(ComentarioPuntoInteresEntity entity) {
        ComentarioPuntoInteresDTO dto = new ComentarioPuntoInteresDTO();
        dto.setId(entity.getId());
        dto.setIdPunto(entity.getIdPunto());
        dto.setIdComentario(entity.getComentario() != null ? entity.getComentario().getId() : null);
        return dto;
    }

    public ComentarioPuntoInteresEntity toEntity(ComentarioPuntoInteresDTO dto) {
        ComentarioPuntoInteresEntity entity = new ComentarioPuntoInteresEntity();
        entity.setId(dto.getId());
        entity.setIdPunto(dto.getIdPunto());

        if (dto.getIdComentario() != null) {
            ComentarioEntity comentario = new ComentarioEntity();
            comentario.setId(dto.getIdComentario());
            entity.setComentario(comentario);
        }

        return entity;
    }
}
