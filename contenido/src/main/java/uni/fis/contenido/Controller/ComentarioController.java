package uni.fis.contenido.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.contenido.dto.ComentarioResponseDTO;
import uni.fis.contenido.dto.CrearComentarioDTO;
import uni.fis.contenido.entity.ComentarioEntity;
import uni.fis.contenido.service.ComentarioService;
import java.util.List;





@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping
    public ResponseEntity<ComentarioEntity> crear(@RequestBody CrearComentarioDTO dto) {
        return ResponseEntity.ok(comentarioService.crearComentario(dto));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorUsuario(@PathVariable String usuario) {
        Long usuarioLong = Long.parseLong(usuario);
        return ResponseEntity.ok(comentarioService.listarPorUsuario(usuarioLong));
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorPublicacion(@PathVariable Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.listarPorPublicacion(idPublicacion));
    }
}
