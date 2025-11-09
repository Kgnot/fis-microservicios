package fis.auth.application.service;

import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.Token;

public interface SignInService {
    Token execute(SignIn signIn);
}
