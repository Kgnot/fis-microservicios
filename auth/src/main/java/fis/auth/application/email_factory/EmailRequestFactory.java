package fis.auth.application.email_factory;

import fis.auth.domain.model.EmailRequest;

public abstract class EmailRequestFactory {

    public abstract EmailRequest create(String email);
}
