package pl.lechowicz.client;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import pl.lechowicz.client.model.Branch;
import pl.lechowicz.client.model.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class GitHubApiClientTest {

    @Inject
    @RestClient
    GitHubApiClient gitHubApiClient;

    @Test
    public void testGetRepositories() {
        List<Repository> repositories = gitHubApiClient.getRepositories("octocat").await().indefinitely();

        assertFalse(repositories.isEmpty());
        Repository firstRepo = repositories.getFirst();
        assertNotNull(firstRepo.name());
        assertEquals("octocat", firstRepo.owner().login());
    }

    @Test
    public void testGetBranches() {
        List<Branch> branches = gitHubApiClient.getBranches("octocat", "Hello-World")
                .await().indefinitely();

        assertFalse(branches.isEmpty());
        Branch firstBranch = branches.getFirst();
        assertNotNull(firstBranch.name());
        assertNotNull(firstBranch.commit().sha());
    }
}
