package fis.auth.infrastructure.controller;


import fis.auth.application.service.LoginService;
import fis.auth.application.service.SignInService;
import fis.auth.domain.model.Login;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.Token;
import fis.auth.infrastructure.dto.request.LoginRequest;
import fis.auth.infrastructure.dto.request.SignInRequest;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import fis.auth.infrastructure.mapper.AuthMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final LoginService loginService;
    private final SignInService signInService;

    public AuthController(
            LoginService loginService,
            SignInService signInService
    ) {
        this.loginService = loginService;
        this.signInService = signInService;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<Token>> login(@RequestBody LoginRequest login) {
        Login loginDomain = AuthMapper.toDomain(login);
        Token token = loginService.execute(loginDomain);
        if (token != null) {
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Inicio de sesión exitoso", token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error al iniciar de sesión", 400));
    }

    @PostMapping("sign-in")
    public ResponseEntity<ApiResponse<Token>> SignIn(@RequestBody SignInRequest signIn) {
        SignIn signInDomain = AuthMapper.toDomain(signIn);
        Token token = signInService.execute(signInDomain);
        if (token != null) {
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Registro exitoso", token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error al iniciar sesión", 400));
    }

}
