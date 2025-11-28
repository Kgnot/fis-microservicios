package fis.auth.infrastructure.endpoints;

import lombok.Getter;

@Getter
public enum EmailEndpoint {
    POST_EMAIL_SEND("/api/email/send");

    private final String endpoint;

    EmailEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}
