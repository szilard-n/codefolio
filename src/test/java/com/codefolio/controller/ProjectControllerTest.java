package com.codefolio.controller;

import com.codefolio.dto.project.NewProjectRequest;
import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.project.ProjectPreviewResponse;
import com.codefolio.dto.task.TaskDto;
import com.codefolio.entity.User;
import com.codefolio.service.JwtService;
import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.IntStream;

import static com.codefolio.util.Constants.PROJECT_1;
import static com.codefolio.util.Constants.PROJECT_2;
import static com.codefolio.util.Constants.USER_1;
import static com.codefolio.util.Constants.USER_2;
import static com.codefolio.util.Constants.USER_3;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@DBRider
@DataSet(
        value = {"database.yml"},
        skipCleaningFor = {"flyway_schema_history"},
        cleanAfter = true,
        cleanBefore = true)
public class ProjectControllerTest {

    @Autowired
    JwtService jwtService;

    @Test
    @DisplayName("Should get all projects without auth")
    public void getAllProjects_successfully() {

        /* with auth */
        var response = given()
                .contentType(ContentType.JSON)
                .get("/api/v1/project")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProjectDto[].class);

        assertThat(response)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    @DisplayName("Preview project successfully")
    public void previewProject_successfully() {

        /* without auth */
        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/project/{projectId}", PROJECT_1.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        /* with auth */
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_1))
                .get("/api/v1/project/{projectId}", PROJECT_1.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProjectPreviewResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo(PROJECT_1.getTitle());
        assertThat(response.description()).isEqualTo(PROJECT_1.getDescription());
        assertThat(response.tasks()).hasSize(2);
    }

    @Test
    @DisplayName("Should preview project and see only own tasks or tasks from project creator")
    public void previewProjectWithCorrectTasks() {
        /* User 1 previewing project 2 -> should see no tasks as none were created by him */
        var response1 = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_1))
                .get("/api/v1/project/{projectId}", PROJECT_2.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProjectPreviewResponse.class);

        assertThat(response1).isNotNull();
        assertThat(response1.tasks()).isEmpty();

        /* User 3 previewing project 2 -> should see one task which was created by him */
        var response2 = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_3))
                .get("/api/v1/project/{projectId}", PROJECT_2.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProjectPreviewResponse.class);

        assertThat(response2).isNotNull();
        assertThat(response2.tasks()).hasSize(1);
    }

    @Test
    @DisplayName("Should successfully assign a project to a user")
    @ExpectedDataSet(value = "project-controller/assignProject-expected.yml",
            compareOperation = CompareOperation.CONTAINS)
    public void assignProject_successfully() {

        /* without auth */
        given()
                .contentType(ContentType.JSON)
                .put("/api/v1/project/assign/{projectId}", PROJECT_1.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        /* with auth */
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_1))
                .put("/api/v1/project/assign/{projectId}", PROJECT_1.getId())
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Should successfully create a project")
    @ExpectedDataSet(value = "project-controller/createProject-expected.yml",
            ignoreCols = {"id", "created_at", "updated_at", "project_id"}, compareOperation = CompareOperation.CONTAINS)
    public void createProject_successfully() {
        List<TaskDto> tasks = IntStream.range(0, 2).boxed()
                .map(value -> new TaskDto("Title " + value, "Description " + value, value + 1))
                .toList();
        var requestBody = new NewProjectRequest("FRONTEND",
                "UI for car shop", "UI for car shop", tasks);

        /* without auth */
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/project")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        /* with auth */
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_1))
                .body(requestBody)
                .post("/api/v1/project")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ProjectDto.class);

        assertThat(response).isNotNull();
        assertThat(response.category()).isEqualTo("FRONTEND");
        assertThat(response.title()).isEqualTo("UI for car shop");
        assertThat(response.description()).isEqualTo("UI for car shop");
        assertThat(response.createdBy()).isEqualTo(USER_1.getId());
    }

    public String createBearerToken(User user) {
        return "Bearer " + jwtService.generateToken(user);
    }
}
