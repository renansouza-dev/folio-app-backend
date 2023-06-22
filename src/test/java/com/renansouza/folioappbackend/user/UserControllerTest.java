package com.renansouza.folioappbackend.user;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@ActiveProfiles(value = {"test"})
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
    }

    @Test
    void shouldNotGetOneUser() {
        given()
                .when()
                .get("/user/{uuid}", UUID.randomUUID())
                .then().log().ifError()
                .statusCode(404)
                .body("message", equalTo("User with provided uuid not found"));
    }

//    @Test
//    void shouldGetOneUser() {
//        var newUser = Instancio.create(User.class);
//        var user = repository.persist(newUser);
//
//        given()
//                .when()
//                .get("/user/{uuid}", user.getUuid())
//                .then()
//                .statusCode(200)
//                .body("uuid", equalTo(newUser.getUuid()))
//                .body("name", equalTo(newUser.getName()))
//                .body("email", equalTo(newUser.getEmail()))
//                .body("picture", equalTo(newUser.getPicture()));
//
//        repository.deleteById(user.getUuid());
//    }

}