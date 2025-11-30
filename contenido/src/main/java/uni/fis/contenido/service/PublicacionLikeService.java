package uni.fis.contenido.service;

import uni.fis.contenido.dto.LikeResponseDTO;

public interface PublicacionLikeService {

    public LikeResponseDTO darLike(Integer idPublicacion, Integer idUsuario);
    public LikeResponseDTO quitarLike(Integer idPublicacion, Integer idUsuario);
}
