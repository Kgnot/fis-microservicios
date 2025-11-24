package uni.fis.contenido.service;

import uni.fis.contenido.dto.CrearComentarioDTO;
import uni.fis.contenido.dto.ComentarioResponseDTO;
import uni.fis.contenido.entity.ComentarioEntity;

import java.util.List;

public interface ComentarioService {
    ComentarioEntity crearComentario(CrearComentarioDTO dto);
    List<ComentarioResponseDTO> listarPorUsuario(Long usuario);
    List<ComentarioResponseDTO> listarPorPublicacion(Long idPublicacion);
}
