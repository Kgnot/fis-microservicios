package uni.fis.foro.controller;

import uni.fis.foro.entity.ForoEntity;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.foro.service.ForoService;
import uni.fis.foro.dto.CrearForoDTO;

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




