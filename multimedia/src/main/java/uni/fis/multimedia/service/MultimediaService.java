package uni.fis.multimedia.service;

import org.springframework.web.multipart.MultipartFile;

import uni.fis.multimedia.dto.MultimediaResponseDTO;
import uni.fis.multimedia.entity.MultimediaEntity;

import java.io.IOException;
import java.util.List;

public interface MultimediaService {

    MultimediaEntity guardarArchivo(MultipartFile archivo) throws IOException;
    List<MultimediaEntity> findAll();
    MultimediaResponseDTO obtenerImagen(Long id);
}
