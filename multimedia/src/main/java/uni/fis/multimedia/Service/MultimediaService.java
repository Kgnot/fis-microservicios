package uni.fis.multimedia.service;

import org.springframework.stereotype.service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.multimedia.dto.CrearMultimediaDTO;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.multimedia.repository.MultimediaRepository;

import java.util.List;


public interface MultimediaService {
    MultimediaEntity guardar(CrearMultimediaDTO dto);
    List<MultimediaEntity> findAll();
    MultimediaEntity obtenerImagen(Long id);
}


@Service
@Transactional
class MultimediaServiceImpl implements MultimediaService {

    private final MultimediaRepository multimediaRepository;

    public MultimediaServiceImpl(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;
    }

    @Override
    public MultimediaEntity guardar(CrearMultimediaDTO dto) {
        MultimediaEntity m = new MultimediaEntity();
        m.setUrl(dto.getUrl());
        m.setTipoArchivo(dto.getTipoArchivo());
        return multimediaRepository.save(m);
    }

    @Override
    public List<MultimediaEntity> findAll() {
        return multimediaRepository.findAll();
    }

    @Override
    public MultimediaEntity obtenerImagen(Long id) {
        return multimediaRepository.findById(id).orElseThrow(() -> new RuntimeException("Multimedia not found"));
    }
}

