package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.Application;
import com.renansouza.folioappbackend.companies.models.CompaniesEntity;
import com.renansouza.folioappbackend.companies.models.CompaniesRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

@Testcontainers
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompaniesControllerIT {

    private CompaniesRequest companiesRequest;
    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    CompaniesRepository companiesRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/v1/companies";
        companiesRequest = Instancio.of(CompaniesRequest.class)
                .set(field(CompaniesRequest::cnpj), "00000000000100")
                .create();
    }

    @AfterEach
    void tearDown() {
        companiesRepository.deleteAll();
    }

    @Test
    @DisplayName("find all successfully")
    void findAll() {
        var company = Instancio.of(CompaniesEntity.class)
                .ignore(all(UUID.class))
                .set(field(CompaniesEntity::getCnpj), companiesRequest.cnpj())
                .create();

        companiesRepository.save(company);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("find all successfully by name desc")
    void findAllOrderedByNameDesc() {
        for (int i = 1; i < 3; i++) {
            var company = Instancio.of(CompaniesEntity.class)
                    .ignore(all(UUID.class))
                    .set(field(CompaniesEntity::getName), "Company " + i)
                    .set(field(CompaniesEntity::getCnpj), "0000000000000" + i)
                    .create();

            companiesRepository.save(company);
        }

        given()
                .contentType(ContentType.JSON)
                .given()
                .queryParam("direction", Sort.Direction.DESC)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalElements", equalTo(2),
                        "totalPages", equalTo(1),
                        "content.name", hasItems("Company 2", "Company 1"),
                        "content.cnpj", hasItems("00000000000002", "00000000000001"));
    }

    @Test
    @DisplayName("find all successfully by cnpj")
    void findAllByCnpj() {
        var company = Instancio.of(CompaniesEntity.class)
                .ignore(all(UUID.class))
                .set(field(CompaniesEntity::getCnpj), companiesRequest.cnpj())
                .create();

        companiesRepository.save(company);

        given()
                .contentType(ContentType.JSON)
                .given()
                .queryParam("cnpj", companiesRequest.cnpj())
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("no company found")
    void noCompanyFound() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("save successfully")
    void save() {
        given()
                .contentType(ContentType.JSON)
                .body(companiesRequest)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("fail to save when cnpj already exists")
    void failToSave() {
        var company = Instancio.of(CompaniesEntity.class)
                .ignore(all(UUID.class))
                .set(field(CompaniesEntity::getCnpj), companiesRequest.cnpj())
                .create();

        companiesRepository.save(company);

        given()
                .contentType(ContentType.JSON)
                .body(companiesRequest)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("update successfully")
    void update() {
        var entity = Instancio.of(CompaniesEntity.class)
                .ignore(all(UUID.class))
                .create();

        var companyId = companiesRepository.save(entity).getUuid();

        var newCompany = Instancio.create(CompaniesRequest.class);

        given()
                .contentType(ContentType.JSON)
                .body(newCompany)
                .when()
                .patch("/{id}", companyId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("fail to update when uuid not found")
    void failToUpdate() {
        var newCompany = Instancio.create(CompaniesRequest.class);

        given()
                .contentType(ContentType.JSON)
                .body(newCompany)
                .when()
                .patch("/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("successfully delete")
    void delete() {
        var entity = Instancio.of(CompaniesEntity.class)
                .ignore(all(UUID.class))
                .create();

        var companyId = companiesRepository.save(entity).getUuid();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/{id}", companyId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("fail to delete when uuid not found")
    void failToDelete() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}