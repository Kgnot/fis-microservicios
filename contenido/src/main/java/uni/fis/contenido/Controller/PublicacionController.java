package uni.fis.contenido.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.contenido.dto.CrearPublicacionDTO;
import uni.fis.contenido.dto.PublicacionResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.service.PublicacionService;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<PublicacionEntity> crear(@RequestBody CrearPublicacionDTO dto) {
        return ResponseEntity.ok(publicacionService.crearPublicacion(dto));
    }

    @GetMapping("/foro/{idForo}")
    public ResponseEntity<List<PublicacionResponseDTO>> listarPorForo(@PathVariable Long idForo) {
        return ResponseEntity.ok(publicacionService.listarPorForo(idForo));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<PublicacionResponseDTO>> listarPorUsuario(@PathVariable String usuario) {
        Long usuarioLong = Long.parseLong(usuario);
        return ResponseEntity.ok(publicacionService.listarPorUsuario(usuarioLong));
    }
}
