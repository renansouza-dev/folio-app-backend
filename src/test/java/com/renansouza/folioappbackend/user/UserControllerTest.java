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
import static org.hamcrest.Matchers.notNullValue;
import static org.instancio.Select.field;

@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"app.security.enable=false"})
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

    @Test
    void shouldGetOneUser() {
        var newUser = Instancio.of(User.class)
                .ignore(field(User::getInvoices))
                .ignore(field(User::getUuid))
                .create();
        var user = repository.persist(newUser);

        given()
                .when()
                .get("/user/{uuid}", user.getUuid())
                .then()
                .statusCode(200)
                .body("uuid", notNullValue(UUID.class))
                .body("name", equalTo(newUser.getName()))
                .body("email", equalTo(newUser.getEmail()))
                .body("picture", equalTo(newUser.getPicture()));

        repository.deleteById(user.getUuid());
    }

}