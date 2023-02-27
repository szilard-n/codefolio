package com.codefolio.controller;

import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.task.CreateTasksRequest;
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

import static com.codefolio.util.Constants.PROJECT_2;
import static com.codefolio.util.Constants.USER_1;
import static com.codefolio.util.Constants.USER_2;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@DBRider
@DataSet(
        value = {"database.yml"},
        skipCleaningFor = {"flyway_schema_history"},
        cleanAfter = true,
        cleanBefore = true)
public class TaskControllerTest {

    @Autowired
    JwtService jwtService;

    @Test
    @DisplayName("Should create tasks for a project")
    @ExpectedDataSet(value = "task-controller/createTasks-expected.yml",
            ignoreCols = {"id", "created_at", "updated_at"}, compareOperation = CompareOperation.CONTAINS)
    public void createTasks_successfully() {
        List<TaskDto> tasks = IntStream.range(0, 2).boxed()
                .map(value -> new TaskDto("Title " + value, "Description " + value, value + 1))
                .toList();
        var requestBody = new CreateTasksRequest(tasks);

        /* without auth */
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/task/{projectId}", PROJECT_2.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        /* with auth */
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_2))
                .body(requestBody)
                .post("/api/v1/task/{projectId}", PROJECT_2.getId())
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    public String createBearerToken(User user) {
        return "Bearer " + jwtService.generateToken(user);
    }
}
