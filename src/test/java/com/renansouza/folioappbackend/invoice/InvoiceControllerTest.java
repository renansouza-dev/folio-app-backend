package com.renansouza.folioappbackend.invoice;

import com.renansouza.folioappbackend.invoice.models.dto.InvoiceDetailRecord;
import com.renansouza.folioappbackend.invoice.models.dto.InvoiceRequest;
import com.renansouza.folioappbackend.invoice.models.entities.Invoice;
import com.renansouza.folioappbackend.invoice.models.entities.InvoiceDetail;
import com.renansouza.folioappbackend.user.User;
import com.renansouza.folioappbackend.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"app.security.enable=false", "logging.level.org.springframework.security=ERROR"})
class InvoiceControllerTest {

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
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    private User user;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;

        user = Instancio.of(User.class)
                .ignore(field(User::getUuid))
                .ignore(field(User::getInvoices))
                .create();

        userRepository.persist(user);
    }

    @AfterEach
    void tearDown() {
        invoiceRepository
                .findByUserEmail(user.getEmail())
                .forEach(invoice -> invoiceRepository.deleteById(invoice.getId()));

        userRepository.delete(user);
    }

//    @Test
//    void get() {
//        var invoice = Instancio.of(Invoice.class)
//                .ignore(all(Long.class))
//                .set(field(Invoice::getUser), user)
//                .generate(field(InvoiceDetail::getAsset), gen -> gen.string().length(6))
//                .generate(field(Invoice::getDate), gen -> gen.temporal().localDate().past())
//                .create();
//
//        invoice.getDetails().forEach(details -> details.setInvoice(invoice));
//
//        var invoiceId = invoiceRepository.persist(invoice).getId();
//
//        given()
//                .when()
//                .get("/invoices", user)
//                .then()
//                .statusCode(200)
//                .body("id", equalTo(List.of(invoiceId.intValue())));
//    }

    @Test
    void getById() {
        var invoice = Instancio.of(Invoice.class)
                .ignore(all(Long.class))
                .set(field(Invoice::getUser), user)
                .generate(field(InvoiceDetail::getAsset), gen -> gen.string().length(6))
                .generate(field(Invoice::getDate), gen -> gen.temporal().localDate().past())
                .create();

        invoice.getDetails().forEach(details -> details.setInvoice(invoice));

        var invoiceId = invoiceRepository.persist(invoice).getId();

        given()
                .when()
                .get("/invoices/{id}", invoiceId)
                .then()
                .statusCode(200)
                .body("id", equalTo(invoiceId.intValue()));
    }

    @Test
    void failToGetById() {
        given()
                .when()
                .get("/invoices/{id}", 1)
                .then()
                .statusCode(404);
    }

//    @Test
//    void getAnEmptyList() {
//        given()
//                .when()
//                .get("/invoices")
//                .then()
//                .statusCode(200)
//                .body("id", equalTo(Collections.EMPTY_LIST));
//    }

    @Test
    void save() {
        var payload = Instancio.of(InvoiceRequest.class)
                .generate(field(InvoiceDetailRecord::asset), gen -> gen.string().length(6))
                .set(field(InvoiceRequest::uuid_user), user.getUuid())
                .create();

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/invoices")
                .then()
                .statusCode(200);
    }

    @Test
    void failToSave() {
        var payload = Instancio.of(InvoiceRequest.class)
                .generate(field(InvoiceDetailRecord::asset), gen -> gen.string().length(6))
                .set(field(InvoiceRequest::uuid_user), UUID.randomUUID())
                .create();

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/invoices")
                .then()
                .statusCode(500);
    }

    @Test
    void delete() {
        var invoice = Instancio.of(Invoice.class)
                .ignore(all(Long.class))
                .set(field(Invoice::getUser), user)
                .generate(field(InvoiceDetail::getAsset), gen -> gen.string().length(6))
                .generate(field(Invoice::getDate), gen -> gen.temporal().localDate().past())
                .create();

        invoice.getDetails().forEach(details -> details.setInvoice(invoice));
        invoiceRepository.persist(invoice);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/invoices/{id}", invoice.getId())
                .then().log().ifError()
                .statusCode(204);
    }

    @Test
    void failToDelete() {
        given()
                .when()
                .delete("/invoices/{id}", "1")
                .then().log().ifError()
                .statusCode(404);
    }

}