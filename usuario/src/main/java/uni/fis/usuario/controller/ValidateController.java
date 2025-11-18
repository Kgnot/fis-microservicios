package uni.fis.usuario.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.request.UserValidateRequest;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.TokenResponse;
import uni.fis.usuario.service.PasswordService;
import uni.fis.usuario.service.UserService;

@RestController
@RequestMapping("/api/v1/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final UserService userService;
    private final PasswordService passwordService;

    @PostMapping("/auth/verify")
    public ResponseEntity<ApiResponse<TokenResponse>> validateUser(@RequestBody UserValidateRequest userValidateRequest) {
        var validate = passwordService.validate(userValidateRequest.id(), userValidateRequest.password());
        if (validate) {
            var user = userService.findById(userValidateRequest.id());
            return user.map(userDto -> ResponseEntity.ok(
                    ApiResponse.success("Usuario verificado",
                            new TokenResponse(
                                    userDto.id(),
                                    userDto.idRol()
                            )
                    )
            )).orElseGet(() -> ResponseEntity
                    .of(ProblemDetail.forStatus(403))
                    .build());
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(403));
    }

}
