package fis.auth.infrastructure.mapper;

import fis.auth.domain.model.Login;
import fis.auth.domain.model.SignIn;
import fis.auth.infrastructure.dto.request.LoginRequest;
import fis.auth.infrastructure.dto.request.SignInRequest;

public class AuthMapper {

    public static Login toDomain(LoginRequest loginRequest) {
        return new Login(loginRequest.name(), loginRequest.password());
    }

    public static SignIn toDomain(SignInRequest signInRequest) {
        return new SignIn(signInRequest.username(), signInRequest.email(), signInRequest.password());
    }
}
