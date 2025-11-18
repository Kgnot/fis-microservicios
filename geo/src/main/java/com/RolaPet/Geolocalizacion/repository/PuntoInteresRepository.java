package com.RolaPet.Geolocalizacion.repository;

import com.RolaPet.Geolocalizacion.domain.entity.PuntoInteres;
import com.RolaPet.Geolocalizacion.domain.enums.TipoPuntoInteres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoInteresRepository extends JpaRepository<PuntoInteres, Long> {

    List<PuntoInteres> findByActivoTrue();

    List<PuntoInteres> findByTipoAndActivoTrue(TipoPuntoInteres tipo);

    /**
     * VERSIÓN SIN POSTGIS: Búsqueda simple por rango de coordenadas
     * Usa fórmula de Haversine aproximada
     */
    @Query("""
        SELECT p FROM PuntoInteres p
        WHERE p.activo = true
        AND (6371 * acos(
            cos(radians(:lat)) * cos(radians(p.latitud)) *
            cos(radians(p.longitud) - radians(:lon)) +
            sin(radians(:lat)) * sin(radians(p.latitud))
        )) <= :radiusKm
        ORDER BY (6371 * acos(
            cos(radians(:lat)) * cos(radians(p.latitud)) *
            cos(radians(p.longitud) - radians(:lon)) +
            sin(radians(:lat)) * sin(radians(p.latitud))
        ))
        """)
    List<PuntoInteres> findNearbyWithoutPostGIS(
            @Param("lat") double latitud,
            @Param("lon") double longitud,
            @Param("radiusKm") double radiusKm
    );

    /**
     * VERSIÓN CON POSTGIS: Más eficiente y precisa
     * Requiere PostGIS extension habilitada
     */
    @Query(value = """
        SELECT * FROM puntos_interes
        WHERE activo = true
        AND ST_DWithin(
            ubicacion::geography,
            ST_MakePoint(:lon, :lat)::geography,
            :radiusMeters
        )
        ORDER BY ST_Distance(
            ubicacion::geography,
            ST_MakePoint(:lon, :lat)::geography
        )
        """, nativeQuery = true)
    List<PuntoInteres> findNearbyWithPostGIS(
            @Param("lat") double latitud,
            @Param("lon") double longitud,
            @Param("radiusMeters") double radiusMeters
    );
}
