package uni.fis.foro.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.foro.Entity.ForoEntity;
import uni.fis.foro.Repository.ForoRepository;
import java.util.List;
import java.util.stream.Collectors;

public interface ForoService {

    ForoEntity crearForo(String nombre);

    List<ForoEntity> listarForos();

    List<ForoEntity> filtrarForos(String nombre);
}

@Service
@Transactional
public class ForoServiceImpl implements ForoService {

    private final ForoRepository foroRepository;

    public ForoServiceImpl(ForoRepository foroRepository) {
        this.foroRepository = foroRepository;
    }

    @Override
    public ForoEntity crearForo(String nombre) {
        ForoEntity foro = new ForoEntity();   
        foro.setNombre(nombre);
        return foroRepository.save(foro);
    }

    @Override
    public List<ForoEntity> listarForos() {
        return foroRepository.findAll();
    }

    @Override
    public List<ForoEntity> filtrarForos(String nombre) {
        return foroRepository.findAll().stream()
                .filter(f -> f.getNombre() != null &&
                             f.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }
}