package pl.lechowicz.restservice.resource;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import pl.lechowicz.restservice.dto.RepositoryDTO;
import pl.lechowicz.restservice.service.GitHubRepositoryService;

@Path("/")
public class GitHubRepositoryResource {

    @Inject
    GitHubRepositoryService gitHubRepositoryService;

    @Path("api/github/repositories/{username}")
    @GET
    public Multi<RepositoryDTO> getRepositories(@PathParam("username") String username) {
        return gitHubRepositoryService.getRepositories(username);
    }
}
