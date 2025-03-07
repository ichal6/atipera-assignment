package pl.lechowicz.restservice.exception.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.lechowicz.restservice.exception.GitHubRepositoryException;
import pl.lechowicz.restservice.exception.model.ErrorResponse;

@Provider
public class GitHubRepositoryExceptionMapper implements ExceptionMapper<GitHubRepositoryException> {

    @Override
    public Response toResponse(GitHubRepositoryException exception) {
        var httpStatus = exception.getStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus, exception.getMessage());
        return Response.status(httpStatus)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}