package pl.lechowicz.restservice.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.lechowicz.client.GitHubApiClient;
import pl.lechowicz.client.exception.ClientException;
import pl.lechowicz.restservice.exception.GitHubRepositoryException;
import pl.lechowicz.restservice.model.BranchDTO;
import pl.lechowicz.restservice.model.RepositoryDTO;

@ApplicationScoped
public class GitHubRepositoryService {
    @Inject
    @RestClient
    GitHubApiClient gitHubApiClient;

    public Multi<RepositoryDTO> getRepositories(String username) {
        return gitHubApiClient.getRepositories(username)
                .onFailure(ClientException.class)
                    .transform(e->
                    {
                        if(e instanceof ClientException clientException) {
                            if(clientException.getStatus() == 404) {
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
                        System.out.println(e.getMessage());
                        return e;
                    })
                .onItem().transformToMulti(repositories -> Multi.createFrom().iterable(repositories))
                .filter(repository -> !repository.fork())
                .onItem().transformToUni(repository -> {
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
                }).merge();
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
