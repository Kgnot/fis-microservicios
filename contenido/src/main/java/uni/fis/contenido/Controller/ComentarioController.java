package uni.fis.contenido.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.contenido.DTO.ComentarioResponseDTO;
import uni.fis.contenido.DTO.CrearComentarioDTO;
import uni.fis.contenido.Entity.Comentario;
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
    public ResponseEntity<Comentario> crear(@RequestBody CrearComentarioDTO dto) {
        return ResponseEntity.ok(comentarioService.crearComentario(dto));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(comentarioService.listarPorUsuario(usuario));
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorPublicacion(@PathVariable Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.listarPorPublicacion(idPublicacion));
    }
}
