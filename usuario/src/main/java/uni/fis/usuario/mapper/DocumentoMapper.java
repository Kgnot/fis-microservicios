package uni.fis.usuario.mapper;

import uni.fis.usuario.dto.request.DocumentoRequest;
import uni.fis.usuario.entity.DocumentoEntity;

public class DocumentoMapper {

    public static DocumentoEntity toEntity(DocumentoRequest request) {

        return new DocumentoEntity(
                request.idDocumento(),
                TipoDocumentoMapper.toEntity(request),
                request.numeroDocumento(),
                request.fechaExpiracion()
        );
    }

}