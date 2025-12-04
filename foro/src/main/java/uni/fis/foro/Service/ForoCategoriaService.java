package uni.fis.foro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.foro.dto.AsignarCategoriaForoDTO;
import uni.fis.foro.entity.ForoCategoriaEntity;
import uni.fis.foro.entity.ForoEntity;
import uni.fis.foro.entity.CategoriaEntity;
import uni.fis.foro.repository.ForoCategoriaRepository;
import uni.fis.foro.repository.ForoRepository;
import uni.fis.foro.repository.CategoriaRepository;

import java.util.List;


public interface ForoCategoriaService {
    ForoCategoriaEntity asignarCategoria(AsignarCategoriaForoDTO dto);
    List<ForoCategoriaEntity> listarPorCategoria(Long idCategoria);
}


@Service
@Transactional
class ForoCategoriaServiceImpl implements ForoCategoriaService {

    private final ForoCategoriaRepository foroCategoriaRepository;
    private final ForoRepository foroRepository;
    private final CategoriaRepository categoriaRepository;

    public ForoCategoriaServiceImpl(
            ForoCategoriaRepository foroCategoriaRepository,
            ForoRepository foroRepository,
            CategoriaRepository categoriaRepository) {
        this.foroCategoriaRepository = foroCategoriaRepository;
        this.foroRepository = foroRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public ForoCategoriaEntity asignarCategoria(AsignarCategoriaForoDTO dto) {

        ForoEntity foro = foroRepository.findById(dto.getIdForo()).orElseThrow();
        CategoriaEntity categoria = categoriaRepository.findById(dto.getIdCategoria()).orElseThrow();

        ForoCategoriaEntity fc = new ForoCategoriaEntity();
        fc.setForo(foro);
        fc.setCategoria(categoria);

        return foroCategoriaRepository.save(fc);
    }

    @Override
    public List<ForoCategoriaEntity> listarPorCategoria(Long idCategoria) {
        CategoriaEntity categoria = categoriaRepository.findById(idCategoria).orElseThrow();
        return foroCategoriaRepository.findByCategoria(categoria);
    }
}

