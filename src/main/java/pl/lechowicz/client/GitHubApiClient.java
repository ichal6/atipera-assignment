package pl.lechowicz.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.lechowicz.client.exception.ClientException;
import pl.lechowicz.client.model.Branch;
import pl.lechowicz.client.model.Repository;

import java.util.List;

@Path("/")
@RegisterRestClient(configKey = "github-api")
public interface GitHubApiClient {
    @GET
    @Path("/users/{username}/repos")
    @Produces("application/vnd.github+json")
    Uni<List<Repository>> getRepositories(@PathParam("username") String username,
                                          @QueryParam("per_page") int perPage,
                                          @QueryParam("page") int pageNumber);

    @GET
    @Path("/repos/{username}/{repo}/branches")
    @Produces("application/vnd.github+json")
    Uni<List<Branch>> getBranches(@PathParam("username") String username, @PathParam("repo") String repo);

    @ClientExceptionMapper
    static ClientException toException(Response response) {
        return new ClientException(
                response.getStatusInfo().getReasonPhrase(),
                response.getStatus()
        );
    }
}
