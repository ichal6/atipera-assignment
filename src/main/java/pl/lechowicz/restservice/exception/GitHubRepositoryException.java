package pl.lechowicz.restservice.exception;

public class GitHubRepositoryException extends RuntimeException {
    private final int status;

    public GitHubRepositoryException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
