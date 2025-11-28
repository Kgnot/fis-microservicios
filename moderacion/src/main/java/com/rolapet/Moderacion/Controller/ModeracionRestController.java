package com.rolapet.Moderacion.Controller;

import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import com.rolapet.Moderacion.Service.ModeracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // 👈 Esto es un controlador REST (recibe peticiones HTTP)
@RequestMapping("/moderacion")  // 👈 Ruta base: http://localhost:8083/api/moderacion
public class ModeracionRestController {

    // Inyectar el servicio
    private final ModeracionService moderacionService;

    public ModeracionRestController(ModeracionService moderacionService) {
        this.moderacionService = moderacionService;
    }

    @PostMapping("/validar")  // 👈 Recibe peticiones POST en /validar
    public ResponseEntity<ModeracionResponseDTO> validarContenido(
            @RequestBody ModeracionRequestDTO request) {  // 👈 Los datos vienen en el body como JSON

        System.out.println("Recibida petición de validación");

        // Llamar al servicio
        ModeracionResponseDTO respuesta = moderacionService.validarContenido(request);

        // Devolver la respuesta (Spring la convierte automáticamente a JSON)
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/palabras")
    public ResponseEntity<PalabraProhibida> agregarPalabra(
            @RequestParam String palabra,  // Parámetro en la URL: ?palabra=xxx
            @RequestParam String descripcion) {

        System.out.println("Recibida petición para agregar palabra");

        PalabraProhibida nueva = moderacionService.agregarPalabraProhibida(palabra, descripcion);

        return ResponseEntity.ok(nueva);
    }
    @GetMapping("/palabras")  // Recibe peticiones GET
    public ResponseEntity<List<PalabraProhibida>> obtenerPalabras() {

        System.out.println("Recibida petición para listar palabras");

        List<PalabraProhibida> palabras = moderacionService.listarTodasLasPalabras();

        return ResponseEntity.ok(palabras);
    }
}
