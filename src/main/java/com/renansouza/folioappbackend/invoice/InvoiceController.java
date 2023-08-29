package com.renansouza.folioappbackend.invoice;

import com.renansouza.folioappbackend.invoice.models.dto.InvoiceRequest;
import com.renansouza.folioappbackend.invoice.models.dto.InvoiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    @Cacheable("usersInvoices")
    @GetMapping
    @Operation(summary = "Find all invoices for the given user UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All invoices founded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvoiceResponse.class)))),
            @ApiResponse(responseCode = "404", description = "No invoices found for the given user")})
    List<InvoiceResponse> findByUserUuid(Principal principal) {
        var email = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        return service.findByUser(String.valueOf(email));
    }

    @CachePut(value = "invoices", key = "#result.id")
    @PostMapping
    @Operation(summary = "Add a new invoice to a user invoice list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The invoice",
                    content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad invoice data was send")})
    InvoiceResponse save(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        return service.save(invoiceRequest);
    }

    @Cacheable("invoices")
    @GetMapping("/{id}")
    @Operation(summary = "Find one invoice for the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One invoice founded",
                    content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
            @ApiResponse(responseCode = "404", description = "No invoice found for the given id")})
    InvoiceResponse findOne(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @CacheEvict(value = "invoices", key = "#id")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invoice from the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The invoice was successfully deleted"),
            @ApiResponse(responseCode = "404", description = "No invoices found for the given id to be deleted")})
    ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}