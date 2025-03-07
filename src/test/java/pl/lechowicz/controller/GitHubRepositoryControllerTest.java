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
                .body("every { it.owner == '" + userName + "' }", is(true));
               // .body("every { it.branches.size() > 0 }", is(true))
               // .body("every { it.branches.every { branch -> branch.name != null && branch.lastCommitSha != null } }", is(true))
               // .body("find { it.name == 'repo1' }.branches", hasItems(
//                        allOf(
//                                hasEntry("name", "main"),
//                                hasKey("lastCommitSha")
//                        ),
//                        allOf(
//                                hasEntry("name", "develop"),
//                                hasKey("lastCommitSha")
//                        )
//                ))
//                .body("find { it.name == 'repo2' }.branches", hasItems(
//                        allOf(
//                                hasEntry("name", "master"),
//                                hasKey("lastCommitSha")
//                        )
//                ));
    }

}
