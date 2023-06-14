package com.renansouza.folioappbackend.user;

import io.restassured.RestAssured;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    UserRepository repository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        repository.deleteAll();
    }

    @Test
    void shouldGetAllUsers() {
        var users = Instancio
                .ofList(User.class)
                .size(2)
                .create();
        repository.saveAll(users);

        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void shouldNotGetAllUsers() {
        given()
                .when()
                .get("/users")
                .then().log().ifStatusCodeIsEqualTo(200)
                .statusCode(404)
                .body("message", equalTo("User with provided uuid not found"));
    }

    @Test
    void shouldGetOneUser() {
        var newUser = Instancio.create(User.class);
        var user = repository.save(newUser);

        given()
                .when()
                .get("/user/{uuid}", user.getUuid())
                .then().log().ifError()
                .statusCode(200)
                .body("name", equalTo(newUser.getName()))
                .body("email", equalTo(newUser.getEmail()));
    }

    @Test
    void shouldNotGetOneUser() {
        given()
                .when()
                .get("/user/{uuid}", UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("message", equalTo("User with provided uuid not found"));
    }

}