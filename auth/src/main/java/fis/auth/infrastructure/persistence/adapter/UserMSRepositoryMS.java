package fis.auth.infrastructure.persistence.adapter;

import fis.auth.application.repository.UserMSRepository;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.TokenRequest;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import fis.auth.infrastructure.endpoints.UserEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserMSRepositoryMS implements UserMSRepository {

    private final RestTemplate restTemplate;

    public UserMSRepositoryMS(@Qualifier("user-ms") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //TODO: Toca hacer algo similar a register user aqui en findNameAndRolUser
    @Override
    public TokenRequest findNameAndRolUser(String name, String password) {
        //necesitamos el nombre y el rol entonces,
        return restTemplate.getForObject(
                UserEndpoint.POST_USER_VALIDATE.getEndpoint(),
                TokenRequest.class);
    }

    @Override
    public TokenRequest registerUser(SignIn signIn) {
        //aqui registramos y esperamos el TokenRequest de parte del usuario
        ResponseEntity<ApiResponse<TokenRequest>> response = restTemplate.exchange(
                UserEndpoint.POST_USER_CREATE.getEndpoint(),
                HttpMethod.POST,
                new HttpEntity<>(signIn),
                new ParameterizedTypeReference<ApiResponse<TokenRequest>>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            TokenRequest tokenResponse = response.getBody().getData();
            return new TokenRequest(tokenResponse.userId(), tokenResponse.rolId());
        }
        return null;
    }
}
