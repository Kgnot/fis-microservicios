package uni.fis.contenido.mapper;

import org.springframework.stereotype.Component;
import uni.fis.contenido.dto.PublicacionResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;

@Component
public class PublicacionMapper {

    public PublicacionResponseDTO toDTO(PublicacionEntity pub) {
        PublicacionResponseDTO dto = new PublicacionResponseDTO();

        dto.setId(pub.getId());
        dto.setTitulo(pub.getTitulo());
        dto.setUsuario(pub.getContenido().getIdAutor());
        dto.setTextoContenido(pub.getContenido().getTexto());
        dto.setFechaContenido(pub.getContenido().getFechaCreacion());
        dto.setIdForo(pub.getForo());
        dto.setIdMultimedia(pub.getMultimedia());

        return dto;
    }
}
