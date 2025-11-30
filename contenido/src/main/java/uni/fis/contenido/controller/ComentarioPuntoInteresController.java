package uni.fis.contenido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.contenido.dto.ComentarioPuntoInteresDTO;
import uni.fis.contenido.service.ComentarioPuntoInteresService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comentarios-punto")
public class ComentarioPuntoInteresController {

    private final ComentarioPuntoInteresService comentarioService;

    public ComentarioPuntoInteresController(ComentarioPuntoInteresService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public ResponseEntity<List<ComentarioPuntoInteresDTO>> getAll() {
        return ResponseEntity.ok(comentarioService.findAll());
    }

    @GetMapping("/punto/{idPunto}")
    public ResponseEntity<List<ComentarioPuntoInteresDTO>> getByPunto(@PathVariable Integer idPunto) {
        return ResponseEntity.ok(comentarioService.findByPunto(idPunto));
    }

    @PostMapping
    public ResponseEntity<ComentarioPuntoInteresDTO> create(@RequestBody ComentarioPuntoInteresDTO dto) {
        return ResponseEntity.ok(comentarioService.create(dto));
    }
}
