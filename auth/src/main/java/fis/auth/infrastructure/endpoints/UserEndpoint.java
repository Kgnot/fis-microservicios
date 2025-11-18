package fis.auth.infrastructure.endpoints;

public enum UserEndpoint {
    POST_USER_VALIDATE("/api/v1/users/auth/verify"),
    POST_USER_CREATE("/api/v1/users");

    private final String endpoint;

    UserEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return this.endpoint;
    }
}
