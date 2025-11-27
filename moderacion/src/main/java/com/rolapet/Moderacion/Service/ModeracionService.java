package com.rolapet.Moderacion.Service;


import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import com.rolapet.Moderacion.Repository.PalabraProhibidaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModeracionService {

    private final PalabraProhibidaRepository palabraRepository;

    // 🗺️ HashMap en memoria para búsquedas ultra rápidas
    // ConcurrentHashMap = Thread-safe (seguro para múltiples usuarios)
    private final Map<String, PalabraProhibida> cachePalabras = new ConcurrentHashMap<>();

    /**
     * 🚀 Se ejecuta AUTOMÁTICAMENTE al iniciar el servicio
     * Carga todas las palabras de la BD al HashMap
     */
    @PostConstruct
    public void inicializarCache() {
        log.info("Cargando palabras prohibidas en memoria...");
        cargarPalabrasEnCache();
        log.info("Cache inicializado con {} palabras", cachePalabras.size());
    }

    /**
     * Carga todas las palabras activas de la BD al HashMap
     */
    private void cargarPalabrasEnCache() {
        List<PalabraProhibida> palabras = palabraRepository.findByActivaTrue();

        cachePalabras.clear(); // Limpiar cache anterior

        for (PalabraProhibida palabra : palabras) {
            // Usar la palabra en minúsculas como key
            cachePalabras.put(palabra.getPalabra().toLowerCase(), palabra);
        }
    }

    /**
     * 🎯 VALIDACIÓN OPTIMIZADA usando HashMap
     * Búsqueda O(1) - Ultra rápida!
     */
    public ModeracionResponseDTO validarContenido(ModeracionRequestDTO request) {
        String contenido = request.getContenido().toLowerCase();
        List<String> palabrasDetectadas = new ArrayList<>();

        // 🚀 Búsqueda ultra rápida en el HashMap
        for (String palabraProhibida : cachePalabras.keySet()) {
            if (contenido.contains(palabraProhibida)) {
                palabrasDetectadas.add(palabraProhibida);
                log.warn("Palabra prohibida detectada: {}", palabraProhibida);
            }
        }

        // Crear respuesta
        if (palabrasDetectadas.isEmpty()) {
            return new ModeracionResponseDTO(
                    true,
                    "Contenido aprobado ✓",
                    0
            );
        } else {
            return new ModeracionResponseDTO(
                    false,
                    "Tu publicación contiene lenguaje inapropiado: " +
                            String.join(", ", palabrasDetectadas),
                    1
            );
        }
    }

    /**
     * ➕ Agregar palabra - Guarda en BD Y actualiza HashMap
     */
    @Transactional
    public PalabraProhibida agregarPalabraProhibida(String palabra, String descripcion) {
        log.info("➕ Agregando palabra: {}", palabra);

        // 1. Guardar en base de datos
        PalabraProhibida nueva = new PalabraProhibida();
        nueva.setPalabra(palabra.toLowerCase());
        nueva.setDescripcion(descripcion);
        nueva.setActiva(true);

        PalabraProhibida guardada = palabraRepository.save(nueva);

        // 2. Actualizar HashMap en memoria
        cachePalabras.put(guardada.getPalabra().toLowerCase(), guardada);

        log.info("Palabra agregada y cache actualizado");
        return guardada;
    }

    /**
     * 🗑️ Eliminar palabra - Elimina de BD Y actualiza HashMap
     */
    @Transactional
    public void eliminarPalabraProhibida(Long id) {
        log.info("🗑️ Eliminando palabra con ID: {}", id);

        // 1. Buscar la palabra
        PalabraProhibida palabra = palabraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palabra no encontrada"));

        // 2. Eliminar de base de datos
        palabraRepository.deleteById(id);

        // 3. Eliminar del HashMap
        cachePalabras.remove(palabra.getPalabra().toLowerCase());

        log.info("Palabra eliminada y cache actualizado");
    }

    /**
     * 🔄 Actualizar palabra - Actualiza BD Y HashMap
     */
    @Transactional
    public PalabraProhibida actualizarPalabraProhibida(Long id, String nuevaPalabra, String descripcion) {
        log.info("🔄 Actualizando palabra con ID: {}", id);

        // 1. Buscar la palabra antigua
        PalabraProhibida palabra = palabraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palabra no encontrada"));

        String palabraAntigua = palabra.getPalabra().toLowerCase();

        // 2. Actualizar en base de datos
        palabra.setPalabra(nuevaPalabra.toLowerCase());
        palabra.setDescripcion(descripcion);
        PalabraProhibida actualizada = palabraRepository.save(palabra);

        // 3. Actualizar HashMap
        cachePalabras.remove(palabraAntigua); // Eliminar la antigua
        cachePalabras.put(actualizada.getPalabra().toLowerCase(), actualizada); // Agregar la nueva

        log.info("✅ Palabra actualizada y cache sincronizado");
        return actualizada;
    }

    /**
     * 🔄 Recargar cache manualmente (útil si hay cambios externos)
     */
    public void recargarCache() {
        log.info("🔄 Recargando cache manualmente...");
        cargarPalabrasEnCache();
        log.info("✅ Cache recargado con {} palabras", cachePalabras.size());
    }

    /**
     * 📊 Obtener estadísticas del cache
     */
    public Map<String, Object> obtenerEstadisticasCache() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("palabrasEnCache", cachePalabras.size());
        stats.put("palabrasEnBD", palabraRepository.count());
        stats.put("cacheActivo", !cachePalabras.isEmpty());
        return stats;
    }

    /**
     * 📋 Listar todas las palabras (desde BD)
     */
    public List<PalabraProhibida> listarTodasLasPalabras() {
        return palabraRepository.findAll();
    }
}