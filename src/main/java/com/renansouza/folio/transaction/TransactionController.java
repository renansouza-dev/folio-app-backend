package com.renansouza.folio.transaction;

import com.renansouza.folio.transaction.exception.TransactionNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/transaction")
@Tag(name = "transaction", description = "the transaction API")
public class TransactionController {

    private final TransactionService service;

    @GetMapping()
    @Operation(summary = "Get a list of transaction.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = Transaction[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    ResponseEntity<Iterable<Transaction>> all() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    ResponseEntity<Transaction> one(@PathVariable("id") long id) throws TransactionNotFoundException {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping()
    @Operation(summary = "Update a transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    ResponseEntity<Void> update(@RequestBody Transaction transaction) throws TransactionNotFoundException {
        service.update(transaction);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a new transaction.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Transaction created",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "409", description = "Transaction already saved")
    })
    ResponseEntity<Transaction> add(@Parameter(description = "Created transaction") @RequestBody Transaction transaction) {
        return new ResponseEntity<>(service.add(transaction), HttpStatus.CREATED);
    }

}