package fis.auth.infrastructure.persistence.adapter;

import fis.auth.application.repository.UserMSRepository;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.TokenRequest;
import fis.auth.infrastructure.dto.request.UserValidateRequest;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import fis.auth.infrastructure.endpoints.UserEndpoint;
import fis.auth.infrastructure.error.NoUserFoundError;
import fis.auth.infrastructure.error.UserMSErrorHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserMSRepositoryMS implements UserMSRepository {

    private final RestTemplate restTemplate;
    private final UserMSErrorHandler errorHandler;

    public UserMSRepositoryMS(@Qualifier("user-ms") RestTemplate restTemplate,
                              UserMSErrorHandler errorHandler) {
        this.restTemplate = restTemplate;
        this.errorHandler = errorHandler;
    }

    @Override
    public TokenRequest findNameAndRolUser(String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin123");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserValidateRequest> requestEntity = new HttpEntity<>(
                new UserValidateRequest(email, password),
                headers
        );

        try {
            ResponseEntity<ApiResponse<TokenRequest>> response = restTemplate
                    .exchange(UserEndpoint.POST_USER_VALIDATE.getEndpoint(),
                            HttpMethod.POST,
                            requestEntity,
                            new ParameterizedTypeReference<>() {});

            if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
                throw NoUserFoundError.of("Respuesta inválida del servicio de usuarios");
            }

            ApiResponse<TokenRequest> body = response.getBody();
            if (body == null || body.getData() == null) {
                throw NoUserFoundError.of("Usuario no encontrado");
            }

            return body.getData();

        } catch (HttpClientErrorException e) {
            errorHandler.handleHttpError(e, new UserValidateRequest(email, password));
            return null; // Nunca llega aquí
        } catch (Exception e) {
            throw NoUserFoundError.of("Error al validar usuario: " + e.getMessage());
        }
    }

    @Override
    public TokenRequest registerUser(SignIn signIn) {
        try {
            ResponseEntity<ApiResponse<TokenRequest>> response = restTemplate.exchange(
                    UserEndpoint.POST_USER_CREATE.getEndpoint(),
                    HttpMethod.POST,
                    new HttpEntity<>(signIn),
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getBody() == null || response.getBody().getData() == null) {
                throw new RuntimeException("Respuesta vacía del servicio de usuarios");
            }

            return response.getBody().getData();

        } catch (HttpClientErrorException e) {
            errorHandler.handleHttpError(e, signIn);
            return null; // Nunca llega aquí
        } catch (Exception e) {
            throw new RuntimeException("Error de comunicación: " + e.getMessage());
        }
    }
}