package uni.fis.foro.Controller;

import uni.fis.foro.Entity.ForoEntity;
import uni.fis.foro.Repository.ForoRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.foro.Service.ForoService;
import uni.fis.foro.DTO.CrearForoDTO;

@RestController
@RequestMapping("/api/foros")
public class ForoController {

    private final ForoService foroService;

    public ForoController(ForoService foroService) {
        this.foroService = foroService;
    }

    @PostMapping
    public ResponseEntity<ForoEntity> crearForo(@RequestBody CrearForoDTO dto) {
        return ResponseEntity.ok(foroService.crearForo(dto.getNombre()));
    }

    @GetMapping
    public ResponseEntity<List<ForoEntity>> listar() {
        return ResponseEntity.ok(foroService.listarForos());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ForoEntity>> filtrar(@RequestParam String nombre) {
        return ResponseEntity.ok(foroService.filtrarForos(nombre));
    }
}




