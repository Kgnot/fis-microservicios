package fis.auth.application.service.impl;


import fis.auth.application.command.TokenCommand;
import fis.auth.application.command.impl.GenerateTokenCommand;
import fis.auth.application.repository.UserMSRepository;
import fis.auth.application.service.LoginService;
import fis.auth.application.service.TokenService;
import fis.auth.domain.model.Login;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import fis.auth.domain.service.EncryptStrategy;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final TokenService tokenService;
    private final UserMSRepository repository;
    private final EncryptStrategy encrypt;

    public LoginServiceImpl(
            TokenService tokenService,
            UserMSRepository repository,
            EncryptStrategy encrypt
    ) {
        this.tokenService = tokenService;
        this.repository = repository;
        this.encrypt = encrypt;
    }
    // cambio para subir la rama
    @Override
    public Token execute(Login login) {
        //Primer paso es encriptar, luego buscar para comparar
        String passwordEncrypted = encrypt.encrypt(login.password());
        //Obtenemos el TokenRequest, es decir, el nombre y rol desde el microservicio de usuario
        TokenRequest req = repository.findNameAndRolUser(login.email(), passwordEncrypted);
        // Luego generamos el token.
        TokenCommand tokenCommand = new GenerateTokenCommand(this.tokenService, req);

        return tokenCommand.execute();
    }
}
