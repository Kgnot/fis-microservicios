package fis.auth.infrastructure.mapper;

import fis.auth.domain.model.Login;
import fis.auth.domain.model.SignIn;
import fis.auth.infrastructure.dto.request.LoginRequest;
import fis.auth.infrastructure.dto.request.SignInRequest;

public class AuthMapper {

    public static Login toDomain(LoginRequest loginRequest) {
        return new Login(loginRequest.email(), loginRequest.password());
    }

    public static SignIn toDomain(SignInRequest signInRequest) {
        return new SignIn(
                signInRequest.name(),
                signInRequest.apellido1(),
                signInRequest.apellido2(),
                signInRequest.fechaNacimiento(),
                DocumentoMapper.toDomain(signInRequest.documento()),
                signInRequest.imgPerfil(), // Si puede ser null
                signInRequest.email(),
                signInRequest.strikes() != null ? signInRequest.strikes() : 0, // Default 0
                signInRequest.idRol(),
                signInRequest.password(),
                signInRequest.emailTutor()
        );
    }
}
