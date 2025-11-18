package com.RolaPet.Geolocalizacion.controller;

import com.RolaPet.Geolocalizacion.domain.dto.*;
import com.RolaPet.Geolocalizacion.domain.enums.TipoPuntoInteres;
import com.RolaPet.Geolocalizacion.service.GeolocalizacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/geolocalizacion")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")  // Permitir CORS
public class GeolocalizacionController {

    private final GeolocalizacionService geolocalizacionService;

    @PostMapping("/geocode")
    public ResponseEntity<CoordenadaDTO> geocodificar(
            @Valid @RequestBody GeocodeRequestDTO request) {

        log.info(" Geocodificando dirección: {}", request.getDireccion());

        CoordenadaDTO coordenada = geolocalizacionService.geocode(request);

        log.info("✅ Coordenadas encontradas: ({}, {})",
                coordenada.getLatitud(), coordenada.getLongitud());

        return ResponseEntity.ok(coordenada);
    }

    @PostMapping("/reverse")
    public ResponseEntity<DireccionDTO> reverseGeocodificar(
            @Valid @RequestBody ReverseGeocodeRequestDTO request) {

        log.info("Reverse geocoding: ({}, {})",
                request.getLatitud(), request.getLongitud());

        DireccionDTO direccion = geolocalizacionService.reverseGeocode(request);

        log.info("Dirección encontrada: {}", direccion.getDireccionCompleta());

        return ResponseEntity.ok(direccion);
    }

    @GetMapping("/buscar")
    public ResponseEntity<CoordenadaDTO> buscarLugar(
            @RequestParam String query) {

        log.info("Buscando lugar: {}", query);

        GeocodeRequestDTO request = new GeocodeRequestDTO();
        request.setDireccion(query);

        CoordenadaDTO coordenada = geolocalizacionService.geocode(request);

        return ResponseEntity.ok(coordenada);
    }
    @GetMapping("/puntos-cercanos")
    public ResponseEntity<List<PuntoInteresDTO>> obtenerPuntosCercanos(
            @RequestParam @Min(-90) @Max(90) double lat,
            @RequestParam @Min(-180) @Max(180) double lon,
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) double radio,
            @RequestParam(required = false) TipoPuntoInteres tipo) {

        log.info("📍 Buscando puntos cercanos: ({}, {}) radio: {}km tipo: {}",
                lat, lon, radio, tipo);

        List<PuntoInteresDTO> puntos = geolocalizacionService.obtenerPuntosCercanos(
                lat, lon, radio, tipo
        );

        log.info("✅ Encontrados {} puntos de interés", puntos.size());

        return ResponseEntity.ok(puntos);
    }
    @PostMapping("/punto-interes")
    //@PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PuntoInteresDTO> registrarPuntoInteres(
            @Valid @RequestBody PuntoInteresRequestDTO request,
            @RequestHeader(value = "X-Usuario-Id", required = false) Long adminId) {

        log.info("Registrando punto de interés: {}", request.getNombre());

        // En producción, obtener adminId del token JWT
        if (adminId == null) {
            adminId = 1L; // Default para desarrollo
        }

        PuntoInteresDTO punto = geolocalizacionService.registrarPuntoInteres(
                request, adminId
        );

        log.info("Punto registrado con ID: {}", punto.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(punto);
    }

    @PutMapping("/punto-interes/{id}")
    //@PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PuntoInteresDTO> actualizarPuntoInteres(
            @PathVariable Long id,
            @Valid @RequestBody PuntoInteresRequestDTO request) {

        log.info("Actualizando punto de interés ID: {}", id);

        PuntoInteresDTO punto = geolocalizacionService.actualizarPuntoInteres(id, request);

        log.info("Punto actualizado exitosamente");

        return ResponseEntity.ok(punto);
    }

    @DeleteMapping("/punto-interes/{id}")
    //@PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarPuntoInteres(@PathVariable Long id) {

        log.info("🗑Eliminando punto de interés ID: {}", id);

        geolocalizacionService.eliminarPuntoInteres(id);

        log.info("Punto eliminado exitosamente");

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/punto-interes/{id}")
    public ResponseEntity<PuntoInteresDTO> obtenerPuntoInteres(@PathVariable Long id) {

        log.info("Consultando punto de interés ID: {}", id);

        PuntoInteresDTO punto = geolocalizacionService.obtenerPuntoInteresPorId(id);

        return ResponseEntity.ok(punto);
    }

    @GetMapping("/puntos-interes")
    public ResponseEntity<List<PuntoInteresDTO>> listarPuntosInteres(
            @RequestParam(required = false) TipoPuntoInteres tipo) {

        log.info("Listando puntos de interés - Tipo: {}", tipo);

        List<PuntoInteresDTO> puntos = geolocalizacionService.listarPuntosInteres(tipo);

        return ResponseEntity.ok(puntos);
    }

    @PostMapping("/calificar")  // Cambiado a POST y sin /{id}
    public ResponseEntity<PuntoInteresDTO> calificarPuntoInteres(
            @RequestBody @Valid CalificacionRequestDTO request,  // ← Ahora recibe el body
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId) {

        log.info("Calificando punto {} con {} estrellas",
                request.getPuntoInteresId(),
                request.getCalificacion());

        if (usuarioId == null) {
            usuarioId = 1L; // Default para desarrollo
        }

        PuntoInteresDTO punto = geolocalizacionService.calificarPuntoInteres(
                request.getPuntoInteresId(),  // ← ID viene del body
                usuarioId,
                request.getCalificacion()     // ← Calificación viene del body
        );

        log.info("Calificación registrada. Promedio: {}",
                punto.getCalificacionPromedio());

        return ResponseEntity.ok(punto);
    }

    // ============================================
    // UBICACIÓN DE USUARIOS
    // ============================================

    /**
     * Actualizar ubicación del usuario (RF14)
     *
     * POST /api/geolocalizacion/ubicacion
     *
     * Body:
     * {
     *   "latitud": 4.710989,
     *   "longitud": -74.072092,
     *   "precision": 10.5
     * }
     */
    @PostMapping("/ubicacion")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> actualizarUbicacion(
            @Valid @RequestBody UbicacionRequestDTO request,
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId) {

        log.info("Actualizando ubicación usuario: {} → ({}, {})",
                usuarioId, request.getLatitud(), request.getLongitud());

        if (usuarioId == null) {
            usuarioId = 1L; // Default para desarrollo
        }

        geolocalizacionService.actualizarUbicacion(
                usuarioId,
                request.getLatitud(),
                request.getLongitud(),
                request.getPrecision()
        );

        log.info("Ubicación actualizada exitosamente");

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Ubicación actualizada correctamente"
        ));
    }

    @GetMapping("/ubicacion/historial")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UbicacionDTO>> obtenerHistorialUbicacion(
            @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId) {

        log.info("Consultando historial de ubicaciones usuario: {}", usuarioId);

        if (usuarioId == null) {
            usuarioId = 1L;
        }

        List<UbicacionDTO> historial = geolocalizacionService.obtenerHistorialUbicacion(usuarioId);

        return ResponseEntity.ok(historial);
    }

    // ============================================
    // CÁLCULO DE DISTANCIAS Y RUTAS
    // ============================================

    /**
     * Calcular distancia entre dos puntos
     * <p>
     * GET /api/geolocalizacion/distancia
     * ?lat1=4.710989&lon1=-74.072092
     * &lat2=4.667321&lon2=-74.055534
     * <p>
     * Response:
     * {
     * "distanciaKm": 5.42,
     * "distanciaMetros": 5420
     * }
     */
    @GetMapping("/distancia")
    public ResponseEntity<Map<String, Number>> calcularDistancia(
            @RequestParam @Min(-90) @Max(90) double lat1,
            @RequestParam @Min(-180) @Max(180) double lon1,
            @RequestParam @Min(-90) @Max(90) double lat2,
            @RequestParam @Min(-180) @Max(180) double lon2) {

        log.info("Calculando distancia: ({},{}) → ({},{})", lat1, lon1, lat2, lon2);

        double distanciaKm = geolocalizacionService.calcularDistancia(lat1, lon1, lat2, lon2);

        return ResponseEntity.ok(Map.of(
                "distanciaKm", Math.round(distanciaKm * 100.0) / 100.0,
                "distanciaMetros", Math.round(distanciaKm * 1000.0)
        ));
    }

    // ============================================
    // HEALTH CHECK Y UTILIDADES
    // ============================================

    /**
     * Health check del servicio
     *
     * GET /api/geolocalizacion/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "servicio", "geolocalizacion-service",
                "version", "1.0.0",
                "timestamp", System.currentTimeMillis()
        ));
    }

    /**
     * Estadísticas del caché
     *
     * GET /api/geolocalizacion/cache/stats
     */
    @GetMapping("/cache/stats")
    //@PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasCache() {
        log.info("Consultando estadísticas del caché");

        Map<String, Object> stats = geolocalizacionService.obtenerEstadisticasCache();

        return ResponseEntity.ok(stats);
    }

    /**
     * Limpiar caché
     *
     * POST /api/geolocalizacion/cache/clear
     */
    @PostMapping("/cache/clear")
    //@PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> limpiarCache() {
        log.info("Limpiando caché de geocodificaciones");

        geolocalizacionService.limpiarCache();

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Cache limpiado exitosamente"
        ));
    }

    /**
     * Validar coordenadas
     *
     * GET /api/geolocalizacion/validar-coordenadas?lat=4.710989&lon=-74.072092
     */
    @GetMapping("/validar-coordenadas")
    public ResponseEntity<Map<String, Object>> validarCoordenadas(
            @RequestParam double lat,
            @RequestParam double lon) {

        boolean validas = geolocalizacionService.validarCoordenadas(lat, lon);

        return ResponseEntity.ok(Map.of(
                "validas", validas,
                "latitud", lat,
                "longitud", lon,
                "mensaje", validas ? "Coordenadas válidas" : "Coordenadas inválidas"
        ));
    }

    // ============================================
    // MANEJO DE ERRORES
    // ============================================

    /**
     * Manejo de excepciones de validación
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarErrorValidacion(
            IllegalArgumentException ex) {

        log.error("Error de validación: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Validación fallida",
                        "mensaje", ex.getMessage()
                ));
    }

    /**
     * Manejo de punto de interés no encontrado
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarErrorGeneral(
            RuntimeException ex) {

        log.error("Error: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Error en el servidor",
                        "mensaje", ex.getMessage()
                ));
    }
}
