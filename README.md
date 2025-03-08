# atipera-assignment

This project provides a REST API that retrieves non-forked repositories and their branches from the GitHub API for a given user. 
It's built using Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Features

*   **Retrieves Non-Forked Repositories:** Fetches only non-forked repositories from GitHub for a specified username.
*   **Fetches Branches:** Retrieves the branches for each non-forked repository, including the branch name and the SHA of the last commit.
*   **Error Handling:** Gracefully handles 404 errors (user not found) and other client exceptions.

## Prerequisites

*   Java 17+
*   Maven 3.9+

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container. 
You should have Docker installed and running on your machine:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/atipera-assignment-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### Accessing the API

Once the application is running, you can access the API at:

    http://localhost:8080/api/github/repositories/{username}

Replace `{username}` with the desired GitHub username.

**Example:**

    http://localhost:8080/api/github/repositories/ichal6

### API Response

The API returns a JSON array of repository objects, each with the following
structure:

```json
[ 
  { 
    "name": "repository-name", 
    "ownerLogin": "username", 
    "branches": 
    [ 
      { 
        "name": "branch-name", 
        "lastCommitSha": "commit-sha"
      } 
    ]
  } 
]
```

In case user do not exist proper response will be returned:

```json
{ 
  "errorMessage": "User nameOfUser not found", 
  "status": 404
}
```

## Project Structure

* `src/main/java/pl/lechowicz/restservice/resource/`: Contains the REST resources source (`GitHubRepositoryResource`).
* `src/main/java/pl/lechowicz/restservice/service/`: Contains the service (`GitHubRepositoryService`) that interacts with the GitHub API.
* `src/main/java/pl/lechowicz/client/`: Contains the REST client interface (`GitHubApiClient`) and related model classes.
* `src/main/java/pl/lechowicz/restservice/dto/`: Contains the DTOs used by the REST API.
* `src/main/resources/application.properties`: Contains application configuration.
* `src/test/java/pl/lechowicz/restservice/resource`: Contains tests for the resource.


## Dependencies

* Quarkus
* MicroProfile Rest Client
* Mutiny
* JUnit
* RestAssured

## Learn More

* Quarkus: https://quarkus.io/
* Quarkus Guides: https://quarkus.io/guides/
* Building native executables https://quarkus.io/guides/maven-tooling
