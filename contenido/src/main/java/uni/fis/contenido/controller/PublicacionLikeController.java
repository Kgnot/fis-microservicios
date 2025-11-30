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

    @PostMapping("/{id}/sumar")
    public ResponseEntity<LikeResponseDTO> sumarLike(@PathVariable Integer id) {
        return ResponseEntity.ok(likeService.sumarLike(id));
    }

    @PostMapping("/{id}/restar")
    public ResponseEntity<LikeResponseDTO> restarLike(@PathVariable Integer id) {
        return ResponseEntity.ok(likeService.restarLike(id));
    }
}
