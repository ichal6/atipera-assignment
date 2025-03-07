package pl.lechowicz.service;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.lechowicz.client.GitHubApiClient;
import pl.lechowicz.model.RepositoryDTO;

import java.util.Collections;

@ApplicationScoped
public class GitHubRepositoryService {
    @Inject
    @RestClient
    GitHubApiClient gitHubApiClient;

    public Multi<RepositoryDTO> getRepositories(String username) {
        return gitHubApiClient.getRepositories(username)
                .onItem().transformToMulti(repositories -> Multi.createFrom().iterable(repositories))
                .filter(repository -> !repository.fork())
                .onItem().transform(repository -> new RepositoryDTO(
                        repository.name(),
                        repository.owner().login(),
                        Collections.emptyList()
                ));
    }
}
