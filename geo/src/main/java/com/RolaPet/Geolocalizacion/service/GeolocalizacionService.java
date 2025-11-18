package com.RolaPet.Geolocalizacion.service;

import com.RolaPet.Geolocalizacion.client.NominatimClient;
import com.RolaPet.Geolocalizacion.domain.dto.*;
import com.RolaPet.Geolocalizacion.domain.entity.PuntoInteres;
import com.RolaPet.Geolocalizacion.domain.entity.Ubicacion;
import com.RolaPet.Geolocalizacion.domain.enums.TipoPuntoInteres;
import com.RolaPet.Geolocalizacion.domain.enums.TipoUbicacion;
import com.RolaPet.Geolocalizacion.repository.PuntoInteresRepository;
import com.RolaPet.Geolocalizacion.repository.UbicacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GeolocalizacionService {

    private final NominatimClient nominatimClient;
    private final PuntoInteresRepository puntoInteresRepository;
    private final UbicacionRepository ubicacionRepository;

    // Cache en memoria (simple)
    private final Map<String, CoordenadaDTO> cacheGeocode = new ConcurrentHashMap<>();
    private final Map<String, DireccionDTO> cacheReverse = new ConcurrentHashMap<>();

    @Value("${geolocalizacion.puntos-interes.radio-maximo-km:50}")
    private double radioMaximoKm;

    @Value("${geolocalizacion.cache.enabled:true}")
    private boolean cacheEnabled;

    // ============================================
    // GEOCODING Y REVERSE GEOCODING
    // ============================================

    /**
     * Geocoding: Dirección → Coordenadas
     */
    public CoordenadaDTO geocode(GeocodeRequestDTO request) {
        String cacheKey = generarCacheKey(request.getDireccion(), request.getCiudad(), request.getPais());

        // Buscar en cache
        if (cacheEnabled && cacheGeocode.containsKey(cacheKey)) {
            log.info("⚡ Cache hit para geocoding: {}", cacheKey);
            return cacheGeocode.get(cacheKey);
        }

        // Construir query
        String query = construirQuery(request);

        // Llamar a Nominatim
        NominatimResponse response = nominatimClient.search(query);

        // Convertir a DTO
        CoordenadaDTO coordenada = CoordenadaDTO.builder()
                .latitud(Double.parseDouble(response.getLat()))
                .longitud(Double.parseDouble(response.getLon()))
                .displayName(response.getDisplay_name())
                .tipo(response.getType())
                .build();

        // Guardar en cache
        if (cacheEnabled) {
            cacheGeocode.put(cacheKey, coordenada);
            log.info("💾 Guardado en cache: {}", cacheKey);
        }

        return coordenada;
    }

    /**
     * Reverse Geocoding: Coordenadas → Dirección
     */
    public DireccionDTO reverseGeocode(ReverseGeocodeRequestDTO request) {
        validarCoordenadas(request.getLatitud(), request.getLongitud());

        String cacheKey = generarCacheKey(request.getLatitud(), request.getLongitud());

        // Buscar en cache
        if (cacheEnabled && cacheReverse.containsKey(cacheKey)) {
            log.info("⚡ Cache hit para reverse geocoding: {}", cacheKey);
            return cacheReverse.get(cacheKey);
        }

        // Llamar a Nominatim
        NominatimResponse response = nominatimClient.reverse(
                request.getLatitud(),
                request.getLongitud()
        );

        // Convertir a DTO
        DireccionDTO direccion = convertirADireccionDTO(response);

        // Guardar en cache
        if (cacheEnabled) {
            cacheReverse.put(cacheKey, direccion);
            log.info("💾 Guardado en cache: {}", cacheKey);
        }

        return direccion;
    }

    // ============================================
    // PUNTOS DE INTERÉS
    // ============================================

    /**
     * Obtener puntos de interés cercanos (RF13)
     */
    public List<PuntoInteresDTO> obtenerPuntosCercanos(
            double latitud,
            double longitud,
            double radioKm,
            TipoPuntoInteres tipo) {

        validarCoordenadas(latitud, longitud);
        validarRadio(radioKm);

        log.info("📍 Buscando puntos cercanos: ({}, {}) radio: {}km tipo: {}",
                latitud, longitud, radioKm, tipo);

        // Obtener puntos desde repositorio
        List<PuntoInteres> puntos = puntoInteresRepository.findNearbyWithoutPostGIS(
                latitud, longitud, radioKm
        );

        // Filtrar por tipo si se especificó
        if (tipo != null) {
            puntos = puntos.stream()
                    .filter(p -> p.getTipo() == tipo)
                    .collect(Collectors.toList());
        }

        // Convertir a DTO y calcular distancia
        return puntos.stream()
                .map(punto -> convertirAPuntoInteresDTO(punto, latitud, longitud))
                .sorted(Comparator.comparingDouble(PuntoInteresDTO::getDistancia))
                .collect(Collectors.toList());
    }

    /**
     * Registrar punto de interés (RF26)
     */
    public PuntoInteresDTO registrarPuntoInteres(PuntoInteresRequestDTO request, Long administradorId) {
        log.info("➕ Registrando punto de interés: {}", request.getNombre());

        validarCoordenadas(request.getLatitud(), request.getLongitud());

        PuntoInteres punto = PuntoInteres.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .tipo(TipoPuntoInteres.valueOf(request.getTipo()))
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .sitioWeb(request.getSitioWeb())
                .imagenUrl(request.getImagenUrl())
                .activo(true)
                .administradorId(administradorId)
                .calificacionPromedio(0.0)
                .totalCalificaciones(0)
                .build();

        PuntoInteres guardado = puntoInteresRepository.save(punto);
        log.info("Punto registrado con ID: {}", guardado.getId());

        return convertirAPuntoInteresDTO(guardado, null, null);
    }

    /**
     * Actualizar punto de interés
     */
    public PuntoInteresDTO actualizarPuntoInteres(Long id, PuntoInteresRequestDTO request) {
        log.info("Actualizando punto de interés ID: {}", id);

        PuntoInteres punto = puntoInteresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + id));

        validarCoordenadas(request.getLatitud(), request.getLongitud());

        // Actualizar campos
        punto.setNombre(request.getNombre());
        punto.setDescripcion(request.getDescripcion());
        punto.setLatitud(request.getLatitud());
        punto.setLongitud(request.getLongitud());
        punto.setTipo(TipoPuntoInteres.valueOf(request.getTipo()));
        punto.setDireccion(request.getDireccion());
        punto.setTelefono(request.getTelefono());
        punto.setSitioWeb(request.getSitioWeb());
        punto.setImagenUrl(request.getImagenUrl());

        PuntoInteres actualizado = puntoInteresRepository.save(punto);
        log.info("Punto actualizado exitosamente");

        return convertirAPuntoInteresDTO(actualizado, null, null);
    }

    /**
     * Eliminar (desactivar) punto de interés
     */
    public void eliminarPuntoInteres(Long id) {
        log.info("Eliminando punto de interés ID: {}", id);

        PuntoInteres punto = puntoInteresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + id));

        // Desactivar en lugar de eliminar
        punto.setActivo(false);
        puntoInteresRepository.save(punto);

        log.info("✅ Punto desactivado exitosamente");
    }

    /**
     * Obtener punto de interés por ID
     */
    public PuntoInteresDTO obtenerPuntoInteresPorId(Long id) {
        log.info("🔍 Consultando punto de interés ID: {}", id);

        PuntoInteres punto = puntoInteresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + id));

        return convertirAPuntoInteresDTO(punto, null, null);
    }

    /**
     * Listar todos los puntos de interés
     */
    public List<PuntoInteresDTO> listarPuntosInteres(TipoPuntoInteres tipo) {
        log.info("📋 Listando puntos de interés - Tipo: {}", tipo);

        List<PuntoInteres> puntos;

        if (tipo != null) {
            puntos = puntoInteresRepository.findByTipoAndActivoTrue(tipo);
        } else {
            puntos = puntoInteresRepository.findByActivoTrue();
        }

        return puntos.stream()
                .map(punto -> convertirAPuntoInteresDTO(punto, null, null))
                .collect(Collectors.toList());
    }

    /**
     * Calificar punto de interés (RF28)
     */
    public PuntoInteresDTO calificarPuntoInteres(Long id, Long usuarioId, double calificacion) {
        log.info("⭐ Calificando punto {} con {} estrellas", id, calificacion);

        PuntoInteres punto = puntoInteresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de interés no encontrado con ID: " + id));

        // Calcular nuevo promedio
        int totalCalificaciones = punto.getTotalCalificaciones();
        double promedioActual = punto.getCalificacionPromedio();

        double nuevoPromedio = ((promedioActual * totalCalificaciones) + calificacion)
                / (totalCalificaciones + 1);

        punto.setCalificacionPromedio(Math.round(nuevoPromedio * 100.0) / 100.0);
        punto.setTotalCalificaciones(totalCalificaciones + 1);

        PuntoInteres actualizado = puntoInteresRepository.save(punto);

        log.info("✅ Calificación registrada. Promedio: {}", actualizado.getCalificacionPromedio());

        return convertirAPuntoInteresDTO(actualizado, null, null);
    }

    // ============================================
    // UBICACIÓN DE USUARIOS
    // ============================================

    /**
     * Actualizar ubicación del usuario (RF14)
     */
    public void actualizarUbicacion(Long usuarioId, double latitud, double longitud, Double precision) {
        validarCoordenadas(latitud, longitud);

        Ubicacion.UbicacionBuilder builder = Ubicacion.builder();
        builder.usuarioId(usuarioId);
        builder.latitud(latitud);
        builder.longitud(longitud);
        builder.precision(precision);
        builder.tipo(TipoUbicacion.ACTUAL);
        builder.timestamp(LocalDateTime.now());
        Ubicacion ubicacion = builder
                .build();

        ubicacionRepository.save(ubicacion);
        log.info("📍 Ubicación actualizada para usuario: {}", usuarioId);
    }

    /**
     * Obtener historial de ubicaciones del usuario
     */
    public List<UbicacionDTO> obtenerHistorialUbicacion(Long usuarioId) {
        log.info("📋 Consultando historial de ubicaciones usuario: {}", usuarioId);

        List<Ubicacion> ubicaciones = ubicacionRepository
                .findByUsuarioIdOrderByTimestampDesc(usuarioId);

        return ubicaciones.stream()
                .map(this::convertirAUbicacionDTO)
                .collect(Collectors.toList());
    }

    // ============================================
    // CÁLCULOS
    // ============================================

    /**
     * Calcular distancia entre dos puntos (fórmula de Haversine)
     */
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distancia en kilómetros
    }

    /**
     * Validar coordenadas
     */
    public boolean validarCoordenadas(double latitud, double longitud) {
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("Latitud inválida: " + latitud + ". Debe estar entre -90 y 90");
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("Longitud inválida: " + longitud + ". Debe estar entre -180 y 180");
        }
        return true;
    }

    // ============================================
    // CACHE
    // ============================================

    /**
     * Obtener estadísticas del caché
     */
    public Map<String, Object> obtenerEstadisticasCache() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("geocodificacionesEnCache", cacheGeocode.size());
        stats.put("direccionesEnCache", cacheReverse.size());
        stats.put("totalEntradas", cacheGeocode.size() + cacheReverse.size());
        stats.put("cacheActivo", cacheEnabled);
        return stats;
    }

    /**
     * Limpiar caché
     */
    public void limpiarCache() {
        cacheGeocode.clear();
        cacheReverse.clear();
        log.info("🧹 Cache limpiado. Geocode: 0, Reverse: 0");
    }

    // ============================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ============================================

    private void validarRadio(double radioKm) {
        if (radioKm <= 0 || radioKm > radioMaximoKm) {
            throw new IllegalArgumentException(
                    "Radio debe estar entre 0 y " + radioMaximoKm + " km"
            );
        }
    }
    private String safeDecode(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        try {
            // Decodificar una vez
            String decoded = URLDecoder.decode(value, StandardCharsets.UTF_8);

            // Si después de decodificar SÍ siguen apareciendo % algo → estaba doble codificada
            if (decoded.contains("%")) {
                decoded = URLDecoder.decode(decoded, StandardCharsets.UTF_8);
            }

            return decoded;
        } catch (Exception e) {
            // Si falla, regresar valor original (mejor que romper)
            return value;
        }
    }

    private String construirQuery(GeocodeRequestDTO request) {

        String direccion = safeDecode(request.getDireccion());
        String ciudad = safeDecode(request.getCiudad());
        String pais = safeDecode(request.getPais());

        log.info("👉 Dirección normalizada: '{}'", direccion);

        StringBuilder query = new StringBuilder(direccion);

        if (ciudad != null && !ciudad.isBlank()) {
            query.append(", ").append(ciudad);
        }
        if (pais != null && !pais.isBlank()) {
            query.append(", ").append(pais);
        }

        return query.toString();
    }


    private String generarCacheKey(Object... params) {
        return String.join("_",
                Arrays.stream(params)
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );
    }

    // ============================================
    // CONVERTIDORES (DTOs)
    // ============================================

    /**
     * Convertir NominatimResponse a DireccionDTO
     */
    private DireccionDTO convertirADireccionDTO(NominatimResponse response) {
        NominatimResponse.Address address = response.getAddress();

        return DireccionDTO.builder()
                .direccionCompleta(response.getDisplay_name())
                .calle(address != null ? address.getRoad() : null)
                .numero(address != null ? address.getHouse_number() : null)
                .ciudad(address != null ?
                        (address.getCity() != null ? address.getCity() :
                                address.getTown() != null ? address.getTown() :
                                        address.getVillage()) : null)
                .departamento(address != null ? address.getState() : null)
                .pais(address != null ? address.getCountry() : null)
                .codigoPostal(address != null ? address.getPostcode() : null)
                .latitud(Double.parseDouble(response.getLat()))
                .longitud(Double.parseDouble(response.getLon()))
                .build();
    }

    /**
     * Convertir PuntoInteres a PuntoInteresDTO
     * ⭐ ESTE ES EL MÉTODO QUE FALTABA
     */
    private PuntoInteresDTO convertirAPuntoInteresDTO(
            PuntoInteres punto,
            Double latitudUsuario,
            Double longitudUsuario) {

        PuntoInteresDTO dto = PuntoInteresDTO.builder()
                .id(punto.getId())
                .nombre(punto.getNombre())
                .descripcion(punto.getDescripcion())
                .latitud(punto.getLatitud())
                .longitud(punto.getLongitud())
                .tipo(punto.getTipo().name())
                .categoria(punto.getCategoria() != null ? punto.getCategoria().name() : null)
                .direccion(punto.getDireccion())
                .telefono(punto.getTelefono())
                .sitioWeb(punto.getSitioWeb())
                .imagenUrl(punto.getImagenUrl())
                .calificacionPromedio(punto.getCalificacionPromedio())
                .totalCalificaciones(punto.getTotalCalificaciones())
                .build();

        // Calcular distancia si se proporcionaron coordenadas del usuario
        if (latitudUsuario != null && longitudUsuario != null) {
            double distancia = calcularDistancia(
                    latitudUsuario,
                    longitudUsuario,
                    punto.getLatitud(),
                    punto.getLongitud()
            );
            dto.setDistancia(Math.round(distancia * 100.0) / 100.0); // Redondear a 2 decimales
        }

        return dto;
    }

    /**
     * Convertir Ubicacion a UbicacionDTO
     */
    private UbicacionDTO convertirAUbicacionDTO(Ubicacion ubicacion) {
        return UbicacionDTO.builder()
                .id(ubicacion.getId())
                .usuarioId(ubicacion.getUsuarioId())
                .latitud(ubicacion.getLatitud())
                .longitud(ubicacion.getLongitud())
                .precision(ubicacion.getPrecision())
                .timestamp(ubicacion.getTimestamp())
                .compartida(ubicacion.getCompartida())
                .tipo(ubicacion.getTipo() != null ? ubicacion.getTipo().name() : null)
                .build();
    }
}