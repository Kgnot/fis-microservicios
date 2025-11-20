package uni.fis.multimedia.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.multimedia.DTO.CrearMultimediaDTO;
import uni.fis.multimedia.Entity.MultimediaEntity;
import uni.fis.multimedia.Repository.MultimediaRepository;

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

