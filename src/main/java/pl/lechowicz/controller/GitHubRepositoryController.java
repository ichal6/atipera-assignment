package pl.lechowicz.controller;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import pl.lechowicz.model.RepositoryDTO;
import pl.lechowicz.service.GitHubRepositoryService;

@Path("/")
public class GitHubRepositoryController {

    @Inject
    GitHubRepositoryService gitHubRepositoryService;

    @Path("api/github/repositories/{username}")
    @GET
    public Multi<RepositoryDTO> getRepositories(@PathParam("username") String username) {
        return gitHubRepositoryService.getRepositories(username);
    }
}
