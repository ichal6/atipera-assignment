package pl.lechowicz.controller;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;



public class GitHubRepositoryControllerTest {
    @Test
    public void testListRepositories() {
        String userName = "octocat";

        given()
                .when().get("/api/github/repositories/" + userName)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("every { it.name != null }", is(true))
                .body("every { it.owner == '" + userName + "' }", is(true))
                .body("every { it.branches.size() > 0 }", is(true))
                .body("every { it.branches.every { branch -> branch.name != null && branch.last_commit_sha != null } }", is(true))
                .body("find { it.name == 'git-consortium' }.branches", hasItems(
                        allOf(
                                hasEntry("name", "master"),
                                hasKey("last_commit_sha")
                        )
                ))
                .body("find { it.name == 'Hello-World' }.branches", hasItems(
                        allOf(
                                hasEntry("name", "master"),
                                hasKey("lastCommitSha")
                        ),
                        allOf(
                                hasEntry("name", "octocat-patch-1"),
                                hasKey("last_commit_sha")
                        ),
                        allOf(
                                hasEntry("name", "test"),
                                hasKey("last_commit_sha")
                        )
                ));
    }

}
