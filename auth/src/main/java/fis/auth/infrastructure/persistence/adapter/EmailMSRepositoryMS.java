package fis.auth.infrastructure.persistence.adapter;

import fis.auth.application.repository.EmailMSRepository;
import fis.auth.domain.model.EmailRequest;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import fis.auth.infrastructure.endpoints.EmailEndpoint;
import fis.auth.infrastructure.error.EmailApiErrorException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Repository
public class EmailMSRepositoryMS implements EmailMSRepository {

    private final RestTemplate restTemplate;

    public EmailMSRepositoryMS(@Qualifier("email-ms") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public void enviarEmailSignIn(EmailRequest emailRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin1234");
        headers.setContentType(MediaType.valueOf(MediaType.TEXT_HTML_VALUE));

        HttpEntity<EmailRequest> request = new HttpEntity<>(
                emailRequest,
                headers
        );

        try {
            ResponseEntity<ApiResponse<Void>> response = restTemplate
                    .exchange(EmailEndpoint.POST_EMAIL_SEND.getEndpoint(),
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<>() {
                            }
                    );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw EmailApiErrorException.of("Error al enviar el mensaje");
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error general en la conexion al microservicio de email");
        }
    }
}
