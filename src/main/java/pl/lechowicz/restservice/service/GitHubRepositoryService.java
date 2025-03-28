package pl.lechowicz.restservice.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.lechowicz.client.GitHubApiClient;
import pl.lechowicz.client.exception.ClientException;
import pl.lechowicz.client.model.Repository;
import pl.lechowicz.restservice.exception.GitHubRepositoryException;
import pl.lechowicz.restservice.dto.BranchDTO;
import pl.lechowicz.restservice.dto.RepositoryDTO;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GitHubRepositoryService {
    private static final int PER_PAGE = 100;

    @Inject
    @RestClient
    GitHubApiClient gitHubApiClient;

    public Multi<RepositoryDTO> getRepositories(String username) {
        return fetchAllRepositories(username, 1, new ArrayList<>(PER_PAGE))
                .onFailure(ClientException.class)
                .transform(e -> handleClientException(username, e))
                .onItem().transformToMulti(repositories -> Multi.createFrom().iterable(repositories))
                .filter(repository -> !repository.fork())
                .onItem().transformToUni(this::transformToUniOfRepositoryDTO)
                .merge();
    }

    private Uni<List<Repository>> fetchAllRepositories(String username, int page, List<Repository> collectedRepos) {
        return gitHubApiClient.getRepositories(username, PER_PAGE, page)
                .onItem().transformToUni(repos -> {
                    collectedRepos.addAll(repos);
                    if (repos.size() < PER_PAGE) {
                        return Uni.createFrom().item(collectedRepos);
                    }
                    return Uni.combine().all().unis(
                            Uni.createFrom().item(collectedRepos),
                            fetchAllRepositories(username, page + 1, new ArrayList<>())
                    ).asTuple().onItem().transform(tuple -> {
                        List<Repository> allRepos = new ArrayList<>(tuple.getItem1());
                        allRepos.addAll(tuple.getItem2());
                        return allRepos;
                    });
                });
    }

    private Uni<RepositoryDTO> transformToUniOfRepositoryDTO(Repository repository) {
        String repoName = repository.name();
        String owner = repository.owner().login();

        Multi<BranchDTO> branches = getBranches(owner, repoName);

        return Uni.combine().all().unis(
                        Uni.createFrom().item(repoName),
                        Uni.createFrom().item(owner),
                        branches.collect().asList()
                ).asTuple()
                .onItem().transform(tuple -> new RepositoryDTO(
                        tuple.getItem1(),
                        tuple.getItem2(),
                        tuple.getItem3()
                ));
    }

    private Throwable handleClientException(String username, Throwable e) {
        if (e instanceof ClientException clientException) {
            if (clientException.getStatus() == 404) {
                return new GitHubRepositoryException(
                        "User " + username + " not found",
                        clientException.getStatus()
                );
            }
            return new GitHubRepositoryException(
                    e.getMessage(),
                    clientException.getStatus()
            );
        }
        return e;
    }

    private Multi<BranchDTO> getBranches(String username, String repoName) {
        return gitHubApiClient.getBranches(username, repoName)
                .onItem().transformToMulti(branches -> Multi.createFrom().iterable(branches))
                .onItem().transform(branch -> new BranchDTO(
                        branch.name(),
                        branch.commit().sha()
                ));
    }
}
