package uni.fis.contenido.service;

import uni.fis.contenido.dto.LikeResponseDTO;

public interface PublicacionLikeService {

    public LikeResponseDTO sumarLike(Integer idPublicacion);
    public LikeResponseDTO restarLike(Integer idPublicacion);
}
