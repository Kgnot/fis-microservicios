package uni.fis.vehiculo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.vehiculo.entities.VehiculoEntity;

public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Integer> {
}
