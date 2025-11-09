package fis.auth.infrastructure.endpoints;

public enum UserEndpoint {
    GET_USER_VALIDATE("/"),
    POST_USER_CREATE("/");

    private final String endpoint;

    private UserEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return this.endpoint;
    }
}
