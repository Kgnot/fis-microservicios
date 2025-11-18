package uni.fis.usuario.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.UserResponse;
import uni.fis.usuario.dto.request.TokenRequest;
import uni.fis.usuario.dto.request.UserValidateRequest;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.service.PasswordService;
import uni.fis.usuario.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;


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

    @PostMapping("/auth/verify")
    public ResponseEntity<?> validateUser(@RequestBody UserValidateRequest userValidateRequest) {
        var validate = passwordService.validate(userValidateRequest.id(), userValidateRequest.password());
        if (validate) {
            var user = userService.findById(userValidateRequest.id());
            if (user.isEmpty()) {
                return ResponseEntity
                        .of(ProblemDetail.forStatus(403))
                        .build();
            }
            return ResponseEntity.ok(
                    new TokenRequest(
                            user.get().id(),
                            user.get().idRol()
                    )
            );
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(403));
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
