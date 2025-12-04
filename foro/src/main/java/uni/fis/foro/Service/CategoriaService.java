package uni.fis.foro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import uni.fis.foro.entity.CategoriaEntity;
import uni.fis.foro.repository.CategoriaRepository;



public interface CategoriaService {
    CategoriaEntity crearCategoria(String nombre);
    List<CategoriaEntity> listarCategorias();
}


@Service
@Transactional
class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaEntity crearCategoria(String nombre) {
        CategoriaEntity c = new CategoriaEntity();
        c.setNombre(nombre);
        return categoriaRepository.save(c);
    }

    @Override
    public List<CategoriaEntity> listarCategorias() {
        return categoriaRepository.findAll();
    }
}