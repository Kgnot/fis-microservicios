package uni.fis.foro.Controller;

import uni.fis.foro.Entity.CategoriaEntity;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.foro.Service.CategoriaService;



@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaEntity> crearCategoria(@RequestBody CategoriaEntity dto) {
        return ResponseEntity.ok(categoriaService.crearCategoria(dto.getNombre()));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaEntity>> listar() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }
}
