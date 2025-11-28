package fis.auth.application.repository;

import fis.auth.domain.model.EmailRequest;

public interface EmailMSRepository {

    void enviarEmailSignIn(EmailRequest emailRequest);
}
