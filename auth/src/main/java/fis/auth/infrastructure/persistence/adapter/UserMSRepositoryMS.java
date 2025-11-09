package fis.auth.infrastructure.persistence.adapter;

import fis.auth.application.repository.UserMSRepository;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.TokenRequest;
import fis.auth.infrastructure.endpoints.UserEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserMSRepositoryMS implements UserMSRepository {

    private final RestTemplate restTemplate;

    public UserMSRepositoryMS(@Qualifier("user-ms") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TokenRequest findNameAndRolUser(String name, String password) {
        //necesitamos el nombre y el rol entonces,
        return restTemplate.getForObject(
                UserEndpoint.GET_USER_VALIDATE.getEndpoint(),
                TokenRequest.class);
    }

    @Override
    public TokenRequest registerUser(SignIn signIn) {
        //aqui registramos y esperamos el TokenRequest de parte del usuario
        return restTemplate.postForObject(
                UserEndpoint.POST_USER_CREATE.getEndpoint(),
                signIn,
                TokenRequest.class
        );
    }
}
