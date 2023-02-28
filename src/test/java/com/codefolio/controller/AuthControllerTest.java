package com.codefolio.controller;

import com.codefolio.dto.auth.AuthResponse;
import com.codefolio.dto.auth.SignInRequest;
import com.codefolio.dto.auth.SignUpRequest;
import com.codefolio.dto.error.ApiErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

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
public class AuthControllerTest {

    @Autowired
    JwtService jwtService;

    @Test
    @DisplayName("Should sign up a user and get the access token")
    @ExpectedDataSet(value = "auth-controller/signUpUser-expected.yml",
            ignoreCols = {"id", "password"}, compareOperation = CompareOperation.CONTAINS)
    public void signUpUser_successfully() {
        var requestBody = new SignUpRequest("username", "email@gmail.com", "pwd123456789");

        var response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/auth/sign-up")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(AuthResponse.class);

        assertThat(response).isNotNull();
        assertThat(jwtService.extractUsername(response.token())).isEqualTo(requestBody.username());
    }

    @Test
    @DisplayName("Should fail to sign up user because fields are invalid")
    public void signUpUser_invalidFields() {
        var requestBody = new SignUpRequest("username", "email", "pass");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/auth/sign-up")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should throw and error when creating a user with already existing username")
    public void sigUpUser_internalError() {
        var requestBody = new SignUpRequest("testUser", "email@gmail.com", "password");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/auth/sign-up")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @Test
    @DisplayName("Should sign in user successfully with username")
    public void signInUserWithUsername_successfully() {
        var requestBody = new SignInRequest("testUser", "password");

        var response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/auth/sign-in")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AuthResponse.class);

        assertThat(response).isNotNull();
        assertThat(jwtService.extractUsername(response.token())).isEqualTo(requestBody.credential());
    }

    @Test
    @DisplayName("Should sign in user successfully with email")
    public void signInUserWithEmail_successfully() {
        var requestBody = new SignInRequest("testUser@gmail.com", "password");

        var response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v1/auth/sign-in")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AuthResponse.class);

        assertThat(response).isNotNull();
        assertThat(jwtService.extractUsername(response.token())).isEqualTo("testUser");
    }

}
