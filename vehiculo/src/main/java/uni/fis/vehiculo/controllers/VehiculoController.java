package uni.fis.vehiculo.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.fis.vehiculo.dto.response.api.ApiResponse;
import uni.fis.vehiculo.entities.VehiculoEntity;
import uni.fis.vehiculo.services.VehiculoService;


@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> registrarVehiculo(@RequestBody VehiculoEntity vehiculoEntity) {
        var vehiculoResponse = vehiculoService.registrarVehiculo(vehiculoEntity);

        if (vehiculoResponse == null) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("No se puede agregar el vehiculo", 400));
        }

        return ResponseEntity.ok(
                ApiResponse.success("El vehiculo fue registrado", vehiculoResponse)
        );
    }


}
