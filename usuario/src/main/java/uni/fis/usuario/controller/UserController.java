package uni.fis.usuario.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.dto.response.ApiResponse;
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
        System.out.println("xd hola: " + userDto);
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
                        ApiResponse.error("Usuario no encontrado", 402)
                ));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@RequestBody UserRequest request) {
        return userService.save(request) ?
                ResponseEntity.ok(ApiResponse.success("Usuario creado exitosamente")) :
                new ResponseEntity<>(
                        ApiResponse.error("Error al crear un usuario", 405),
                        HttpStatusCode.valueOf(405));
    }

}
