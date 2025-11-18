package fis.auth.infrastructure.endpoints;

public enum UserEndpoint {
    GET_USER_VALIDATE("/api/v1/users/validate"),
    POST_USER_CREATE("/api/v1/users");

    private final String endpoint;

    private UserEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return this.endpoint;
    }
}
