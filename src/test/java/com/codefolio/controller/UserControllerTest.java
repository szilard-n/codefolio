package com.codefolio.controller;

import com.codefolio.dto.user.UserDto;
import com.codefolio.dto.user.UserPreviewResponse;
import com.codefolio.entity.User;
import com.codefolio.service.JwtService;
import com.github.database.rider.core.api.dataset.DataSet;
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

import static com.codefolio.util.Constants.USER_1;
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
public class UserControllerTest {

    @Autowired
    JwtService jwtService;

    @Test
    @DisplayName("Should fetch all users")
    public void getAllUsers_successfully() {

        /* without auth */
        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/user")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        /* with auth */
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_1))
                .get("/api/v1/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto[].class);

        assertThat(response)
                .isNotNull()
                .hasSize(3);
    }

    @Test
    @DisplayName("Should fetch a specific user preview")
    public void previewUser_successfully() {

        /* without auth */
        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/user/{userId}", USER_1.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        /* with auth */
        var response = given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(USER_1))
                .get("/api/v1/user/{userId}", USER_1.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserPreviewResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(USER_1.getUsername());
    }

    @Test
    @DisplayName("Should validate credential without auth")
    public void validateCredentials_successfully() {

        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/user/validate/{credential}", USER_1.getEmail())
                .then()
                .statusCode(HttpStatus.CONFLICT.value());

        given()
                .contentType(ContentType.JSON)
                .get("/api/v1/user/validate/{credential}", "randomUsername")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    private String createBearerToken(User user) {
        return "Bearer " + jwtService.generateToken(user);
    }
}
