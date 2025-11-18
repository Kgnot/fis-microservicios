package uni.fis.usuario.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.TokenResponse;
import uni.fis.usuario.dto.response.UserResponse;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jwt/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        List<UserDto> userDto = userService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Usuarios encontrados",
                        userDto.stream()
                                .map(UserMapper::dtoToResponse)
                                .toList()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(userDto -> ResponseEntity.ok(
                        ApiResponse.success(
                                "Usuario encontrado",
                                UserMapper.dtoToResponse(userDto)
                        )
                ))
                .orElseGet(() -> ResponseEntity.status(404).body(
                        ApiResponse.error("Usuario no encontrado", 404) // ← 404 en lugar de 402
                ));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<TokenResponse>> create(@RequestBody UserRequest request) {
        try {
            UserDto userDto = userService.save(request);
            return ResponseEntity.ok(ApiResponse.success("Usuario creado exitosamente",
                    new TokenResponse(userDto.id(), userDto.idRol())));
        } catch (Exception e) {
            return ResponseEntity.status(400).body( // ← 400 para errores de validación
                    ApiResponse.error("Error al crear usuario: " + e.getMessage(), 400)
            );
        }
    }
}