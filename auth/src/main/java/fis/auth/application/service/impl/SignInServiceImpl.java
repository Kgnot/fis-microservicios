package fis.auth.application.service.impl;

import fis.auth.application.repository.UserMSRepository;
import fis.auth.application.service.SignInService;
import fis.auth.application.service.TokenService;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import fis.auth.domain.service.EncryptStrategy;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl implements SignInService {

    private final TokenService tokenService;
    private final UserMSRepository repository;
    private final EncryptStrategy encrypt;

    public SignInServiceImpl(
            TokenService tokenService,
            UserMSRepository repository,
            EncryptStrategy encrypt
    ) {
        this.tokenService = tokenService;
        this.repository = repository;
        this.encrypt = encrypt;
    }

    @Override
    public Token execute(SignIn signIn) {
        //primero validamos si es un signIn correcto.
        // segundo, encriptamos
        String passwordEncrypted = encrypt.encrypt(signIn.password());
        // tercero agregamos a la base de datos:
        TokenRequest tokenRequest = repository.registerUser(signIn);
        if (tokenRequest != null) {
            return tokenService.generateToken(tokenRequest);
        }
        // retornamos nullo, aunque podriamos retonrar con Optional
        return null;
    }
}
