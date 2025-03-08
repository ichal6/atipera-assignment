package pl.lechowicz.restservice.resource;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GitHubRepositoryResourceTest {
    @Test
    public void testListRepositoriesForIchal6User() {
        // Arrange
        String userName = "ichal6";
        int maxNumberOfResultsPerPageForGitHubAPI = 100;

        // Act and Assert
        given()
                .when().get("/api/github/repositories/" + userName)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(maxNumberOfResultsPerPageForGitHubAPI))
                .body("every { it.name != null }", is(true))
                .body("every { it.ownerLogin == '" + userName + "' }", is(true))
                .body("every { it.branches.size() > 0 }", is(true))
                .body("every { it.branches.every { branch -> branch.name != null && branch.lastCommitSha != null } }",
                        is(true));
    }

}
