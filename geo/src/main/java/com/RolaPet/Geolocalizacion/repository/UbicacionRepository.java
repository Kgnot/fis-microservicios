package com.RolaPet.Geolocalizacion.repository;

import com.RolaPet.Geolocalizacion.domain.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    List<Ubicacion> findByUsuarioIdOrderByTimestampDesc(Long usuarioId);

    List<Ubicacion> findByUsuarioIdAndTimestampBetween(
            Long usuarioId,
            LocalDateTime inicio,
            LocalDateTime fin
    );

    void deleteByUsuarioIdAndTimestampBefore(Long usuarioId, LocalDateTime fecha);
}
