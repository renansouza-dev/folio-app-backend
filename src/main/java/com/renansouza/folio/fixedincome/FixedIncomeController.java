package com.renansouza.folio.fixedincome;

import com.renansouza.folio.fixedincome.entities.FixedIncome;
import com.renansouza.folio.fixedincome.exception.FixedIncomeNotFoundException;
import com.renansouza.folio.user.User;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/fixed_income")
@Tag(name = "fixed income", description = "the fixed income API")
public class FixedIncomeController {

    private final FixedIncomeService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a list of fixed income.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = FixedIncome[].class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    ResponseEntity<List<FixedIncome>> all() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get an fixed income.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = FixedIncome.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Fixed income not found")
    })
    ResponseEntity<FixedIncome> one(@PathVariable("id") long id) throws FixedIncomeNotFoundException {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a new fixed income.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Fixed income created",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Fixed income not found")
    })
    ResponseEntity<FixedIncome> add(@Parameter(description = "Created fixed income") @RequestBody FixedIncome fixedIncome) {
        return new ResponseEntity<>(service.add(fixedIncome), HttpStatus.CREATED);
    }

}