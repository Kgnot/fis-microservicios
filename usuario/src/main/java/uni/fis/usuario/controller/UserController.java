package uni.fis.usuario.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.UserResponse;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
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
                        ApiResponse.error("Usuario no encontrado",402)
                ));
    }

}
