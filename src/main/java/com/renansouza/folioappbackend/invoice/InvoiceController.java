package com.renansouza.folioappbackend.invoice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService service;

    @PostMapping
    @Operation(summary = "Add a new invoice to a user invoice list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The invoice",
                    content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad invoice data was send")})
    InvoiceResponse save(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        return service.save(invoiceRequest);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Find all invoices for the given user UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All invoices founded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvoiceResponse.class)))),
            @ApiResponse(responseCode = "404", description = "No invoices found for the given user")})
    List<InvoiceResponse> findAllByUserUuid(@PathVariable("uuid") UUID uuid) {
        return service.findAllByUserUuid(uuid);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an invoice already saved.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The invoice was successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad invoice data was send"),
            @ApiResponse(responseCode = "404", description = "No invoices found for the given id")})
    void update(@PathVariable("id") Long id, @Valid @RequestBody InvoiceRequest invoiceRequest) {
        service.update(id, invoiceRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invoice from the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The invoice was successfully deleted"),
            @ApiResponse(responseCode = "404", description = "No invoices found for the given id to be deleted")})
    void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

}