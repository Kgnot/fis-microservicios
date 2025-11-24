package uni.fis.multimedia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.multimedia.dto.CrearMultimediaDTO;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.multimedia.service.MultimediaService;
import java.util.List;



@RestController
@RequestMapping("/api/multimedia")
public class MultimediaController {

    private final MultimediaService multimediaService;

    public MultimediaController(MultimediaService multimediaService) {
        this.multimediaService = multimediaService;
    }

    @PostMapping
    public ResponseEntity<MultimediaEntity> crear(@RequestBody CrearMultimediaDTO dto) {
        return ResponseEntity.ok(multimediaService.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MultimediaEntity>> listar() {
        return ResponseEntity.ok(multimediaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultimediaEntity> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(multimediaService.obtenerImagen(id));
    }
}
