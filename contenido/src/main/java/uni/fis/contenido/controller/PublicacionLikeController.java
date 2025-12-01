package uni.fis.contenido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uni.fis.contenido.dto.LikeResponseDTO;
import uni.fis.contenido.service.PublicacionLikeService;

@RestController
@RequestMapping("/api/v1/publicaciones/likes")
public class PublicacionLikeController {

    private final PublicacionLikeService likeService;

    public PublicacionLikeController(PublicacionLikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/dar")
    public ResponseEntity<LikeResponseDTO> darLike(
            @RequestParam Integer publicacion,
            @RequestParam Integer usuario
    ) {
        return ResponseEntity.ok(likeService.darLike(publicacion, usuario));
    }

    @PostMapping("/quitar")
    public ResponseEntity<LikeResponseDTO> quitarLike(
            @RequestParam Integer publicacion,
            @RequestParam Integer usuario
    ) {
        return ResponseEntity.ok(likeService.quitarLike(publicacion, usuario));
    }
}
