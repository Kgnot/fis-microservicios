package com.rolapet.Moderacion.Controller;

import com.rolapet.Moderacion.Domain.dto.ApiResponse;
import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import com.rolapet.Moderacion.Service.ModeracionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moderacion")
@RequiredArgsConstructor
@Slf4j
public class ModeracionRestController {

    // Servicio encargado de la lógica de moderación de contenido
    private final ModeracionService moderacionService;

    /**
     * Validar contenido enviado por un usuario.
     * Verifica si contiene palabras prohibidas y retorna el resultado.
     */
    @PostMapping("/validar")
    public ResponseEntity<ApiResponse<ModeracionResponseDTO>> validarContenido(
            @RequestBody ModeracionRequestDTO request) {

        log.info("Iniciando validación de contenido para usuario: {}", request.getUsuarioId());
        log.debug("Contenido a validar: {}", request.getContenido());

        try {
            // Procesa el contenido y obtiene la respuesta
            ModeracionResponseDTO respuesta = moderacionService.validarContenido(request);

            log.info("Validación completada. Aprobado: {}, Infracciones: {}",
                    respuesta.getAprobado(), respuesta.getNumeroInfracciones());

            // Mensaje según el resultado
            String mensaje = respuesta.getAprobado()
                    ? "Contenido aprobado correctamente"
                    : "Contenido rechazado por infracciones";

            return ResponseEntity.ok(
                    ApiResponse.success(mensaje, respuesta)
            );

        } catch (Exception e) {
            // Manejo de error inesperado
            log.error("Error al validar contenido: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al validar contenido: " + e.getMessage(), 500));
        }
    }

    /**
     * Agregar una palabra prohibida al sistema.
     * Se recibe por parámetros para facilitar pruebas desde Postman.
     */
    @PostMapping("/palabras")
    public ResponseEntity<ApiResponse<PalabraProhibida>> agregarPalabra(
            @RequestParam String palabra,
            @RequestParam String descripcion) {

        log.info("Intentando agregar nueva palabra prohibida: '{}'", palabra);

        try {
            // Crea y almacena una nueva palabra prohibida
            PalabraProhibida nueva = moderacionService.agregarPalabraProhibida(palabra, descripcion);

            log.info("Palabra prohibida agregada exitosamente con ID: {}", nueva.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Palabra prohibida agregada y cache actualizado", nueva));

        } catch (IllegalArgumentException e) {
            // Error por palabra duplicada o inválida
            log.warn("Intento de agregar palabra duplicada o inválida: '{}'", palabra);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), 400));

        } catch (Exception e) {
            // Error inesperado
            log.error("Error al agregar palabra prohibida: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al agregar palabra prohibida", 500));
        }
    }

    /**
     * Obtener todas las palabras prohibidas almacenadas.
     */
    @GetMapping("/palabras")
    public ResponseEntity<ApiResponse<List<PalabraProhibida>>> obtenerPalabras() {

        log.info("Solicitando listado de palabras prohibidas");

        try {
            // Consulta completa de palabras prohibidas
            List<PalabraProhibida> palabras = moderacionService.listarTodasLasPalabras();

            log.info("Se encontraron {} palabras prohibidas", palabras.size());

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Palabras obtenidas correctamente (" + palabras.size() + " encontradas)",
                            palabras
                    )
            );

        } catch (Exception e) {
            // Error inesperado
            log.error("Error al obtener palabras prohibidas: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener palabras prohibidas", 500));
        }
    }

    /**
     * Actualizar una palabra prohibida por ID.
     */
    @PutMapping("/palabras/{id}")
    public ResponseEntity<ApiResponse<PalabraProhibida>> actualizarPalabra(
            @PathVariable Integer id,
            @RequestParam String palabra,
            @RequestParam String descripcion) {

        log.info("Intentando actualizar palabra con ID: {}", id);

        try {
            // Actualiza la palabra si existe
            PalabraProhibida actualizada = moderacionService.actualizarPalabraProhibida(id, palabra, descripcion);

            log.info("Palabra actualizada exitosamente: '{}'", actualizada.getPalabra());

            return ResponseEntity.ok(
                    ApiResponse.success("Palabra actualizada correctamente", actualizada)
            );

        } catch (RuntimeException e) {
            // Error si el ID no existe
            log.warn("Error al actualizar palabra ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), 404));

        } catch (Exception e) {
            // Error inesperado
            log.error("Error inesperado al actualizar palabra: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar palabra", 500));
        }
    }

    /**
     * Eliminar una palabra prohibida por ID.
     */
    @DeleteMapping("/palabras/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPalabra(@PathVariable Integer id) {

        log.info("Intentando eliminar palabra con ID: {}", id);

        try {
            // Eliminación en base de datos y actualización de caché
            moderacionService.eliminarPalabraProhibida(id);

            log.info("Palabra eliminada exitosamente");

            return ResponseEntity.ok(
                    ApiResponse.success("Palabra eliminada correctamente")
            );

        } catch (RuntimeException e) {
            // Error por ID inexistente
            log.warn("Error al eliminar palabra ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), 404));

        } catch (Exception e) {
            // Error inesperado
            log.error("Error inesperado al eliminar palabra: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar palabra", 500));
        }
    }
}
