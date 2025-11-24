package uni.fis.vehiculo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.fis.vehiculo.entities.VehiculoEntity;
import uni.fis.vehiculo.repository.VehiculoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private VehiculoRepository repository;


    @Override
    public VehiculoEntity registrarVehiculo(VehiculoEntity entity) {
        return repository.save(entity);
    }
}
