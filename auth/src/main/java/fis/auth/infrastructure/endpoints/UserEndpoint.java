package fis.auth.infrastructure.endpoints;

import lombok.Getter;

@Getter
public enum UserEndpoint {
    POST_USER_VALIDATE("/api/v1/validate/auth/verify"),
    POST_USER_CREATE("/api/v1/users"),
    GET_USER_ID_BY_EMAIL("/api/v1/users");

    private final String endpoint;

    UserEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}
