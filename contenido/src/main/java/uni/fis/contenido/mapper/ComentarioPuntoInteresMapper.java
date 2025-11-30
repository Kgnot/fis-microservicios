package uni.fis.contenido.mapper;

import org.springframework.stereotype.Component;
import uni.fis.contenido.dto.ComentarioPuntoInteresDTO;
import uni.fis.contenido.entity.ComentarioPuntoInteresEntity;
import uni.fis.contenido.entity.ContenidoEntity;

@Component
public class ComentarioPuntoInteresMapper {

    public ComentarioPuntoInteresDTO toDTO(ComentarioPuntoInteresEntity entity) {
        ComentarioPuntoInteresDTO dto = new ComentarioPuntoInteresDTO();
        dto.setId(entity.getId());
        dto.setIdPunto(entity.getIdPunto());
        dto.setIdContenido(entity.getContenido() != null ? entity.getContenido().getId() : null);
        return dto;
    }

    public ComentarioPuntoInteresEntity toEntity(ComentarioPuntoInteresDTO dto) {
        ComentarioPuntoInteresEntity entity = new ComentarioPuntoInteresEntity();
        entity.setId(dto.getId());
        entity.setIdPunto(dto.getIdPunto());

        if (dto.getIdContenido() != null) {
            ContenidoEntity contenido = new ContenidoEntity();
            contenido.setId(dto.getIdContenido());
            entity.setContenido(contenido);
        }

        return entity;
    }
}
