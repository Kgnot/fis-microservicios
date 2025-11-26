package fis.auth.application.repository;

import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.TokenRequest;

public interface UserMSRepository {
    TokenRequest findNameAndRolUser(
            String name,
            String password
    );

    TokenRequest registerUser(
            SignIn signIn
    );

    Integer findUserIdByEmail(
            String email
    );
}
