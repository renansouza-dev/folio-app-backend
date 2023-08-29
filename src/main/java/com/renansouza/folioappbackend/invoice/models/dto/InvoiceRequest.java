package com.renansouza.folioappbackend.invoice.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InvoiceRequest(
        @NotBlank
        UUID uuid_user,
        @PastOrPresent
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate date,
        @NotBlank
        String broker,
        @NotBlank
        BigDecimal settlement,
        List<@NotNull InvoiceDetailRecord> details
) {
}