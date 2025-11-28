package fis.auth.application.service.impl;

import fis.auth.application.email_factory.EmailRequestFactory;
import fis.auth.application.error.MessageSendException;
import fis.auth.application.repository.EmailMSRepository;
import fis.auth.application.repository.UserMSRepository;
import fis.auth.application.service.SignInService;
import fis.auth.application.service.TokenService;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import fis.auth.domain.service.EncryptStrategy;
import fis.auth.domain.usecase.MenorDeEdadUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl implements SignInService {

    private final TokenService tokenService;
    private final UserMSRepository userRepository;
    private final EncryptStrategy encrypt;
    private final EmailMSRepository emailRepository;
    private final EmailRequestFactory emailRequestFactory;

    public SignInServiceImpl(
            TokenService tokenService,
            UserMSRepository userRepository,
            EncryptStrategy encrypt,
            EmailMSRepository emailRepository,
            @Qualifier("email-aprobacion") EmailRequestFactory emailRequestFactory
    ) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.encrypt = encrypt;
        this.emailRepository = emailRepository;
        this.emailRequestFactory = emailRequestFactory;
    }

    @Override
    public Token execute(SignIn signIn) {
        //validamos la edad
        boolean esMenor = MenorDeEdadUseCase.execute(signIn);
        if (esMenor) {
            // debemos crear el emailRequest xd
            emailRepository
                    .enviarEmailSignIn(emailRequestFactory.create(signIn.emailTutor()));
            throw new MessageSendException("Correo enviado para aprobación");
        }
        //Encriptamos
        String passwordEncrypted = encrypt.encrypt(signIn.password());
        // Agregamos a la base de datos
        TokenRequest tokenRequest = userRepository
                .registerUser(signIn.withPassword(passwordEncrypted));
        if (tokenRequest != null) {
            return tokenService.generateToken(tokenRequest);
        }
        // retornamos nullo, aunque podriamos retonrar con Optional
        return null;
    }

}
