package com.renansouza.folioappbackend.company;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"app.security.enable=false", "logging.level.org.springframework.security=ERROR"})
class CompanyControllerTest {

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
    CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void get() {
        var company = Instancio.of(Company.class)
                .ignore(all(Long.class))
                .create();

        var companyId = companyRepository.persist(company).getId();

        given()
                .when()
                .queryParam("id", companyId)
                .get("/companies")
                .then()
                .statusCode(200)
                .body("id", equalTo(companyId.intValue()));

        given()
                .when()
                .queryParam("cnpj", company.getCnpj())
                .get("/companies")
                .then()
                .statusCode(200)
                .body("cnpj", equalTo(company.getCnpj()));

        given()
                .when()
                .queryParam("ticker", company.getTickers().get(0))
                .get("/companies")
                .then()
                .statusCode(200);
    }

    @Test
    void save() {
        var payload = Instancio.create(CompanyRequest.class);

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/companies")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void failToSave() {
        var payload = Instancio.create(CompanyRequest.class);

        var company = Instancio.of(Company.class)
                .ignore(all(Long.class))
                .set(field(Company::getCnpj), payload.cnpj())
                .create();

        companyRepository.persist(company);

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/companies")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void update() {
        var company = Instancio.of(Company.class)
                .ignore(all(Long.class))
                .create();

        var payload = companyRepository.persist(company);
        payload.getTickers().set(0, "ABCD12");

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch("/companies")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete() {
        var company = Instancio.of(Company.class)
                .ignore(all(Long.class))
                .create();

        var companyId = companyRepository.persist(company).getId();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/companies/{id}", companyId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void failToDelete() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/companies/{id}", 1)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}