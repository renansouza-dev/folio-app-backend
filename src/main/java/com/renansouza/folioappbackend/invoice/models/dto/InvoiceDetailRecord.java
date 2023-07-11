package com.renansouza.folioappbackend.invoice.models.dto;

import com.renansouza.folioappbackend.invoice.models.InvoiceOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record InvoiceDetailRecord(
        @NotBlank
        @Size(min = 6, max = 6)
        String asset,
        @NotBlank
        InvoiceOperation operation,
        @Positive
        int quantity,
        @Positive
        BigDecimal price
) {
}
