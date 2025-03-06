package pl.lechowicz.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.lechowicz.client.model.Branch;
import pl.lechowicz.client.model.Repository;

import java.util.List;

@Path("/")
@RegisterRestClient(configKey = "github-api")
public interface GitHubApiClient {
    @GET
    @Path("/users/{username}/repos")
    @Produces("application/vnd.github+json")
    List<Repository> getRepositories(@PathParam("username") String username);

    @GET
    @Path("/repos/{username}/{repo}/branches")
    @Produces("application/vnd.github+json")
    List<Branch> getBranches(@PathParam("username") String username, @PathParam("repo") String repo);
}
