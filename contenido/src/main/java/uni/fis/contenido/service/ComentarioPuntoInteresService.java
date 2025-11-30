package uni.fis.contenido.service;

import uni.fis.contenido.dto.ComentarioPuntoInteresDTO;
import java.util.List;

public interface ComentarioPuntoInteresService {

    List<ComentarioPuntoInteresDTO> findAll();

    List<ComentarioPuntoInteresDTO> findByPunto(Integer idPunto);

    ComentarioPuntoInteresDTO create(ComentarioPuntoInteresDTO comentarioDTO);
}
