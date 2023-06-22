package com.renansouza.folioappbackend.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

record InvoiceRequest(
        @NotBlank
        UUID uuid_user,
        @PastOrPresent
        @JsonFormat(pattern="dd/MM/yyyy")
        LocalDate date,
        @NotBlank
        String broker,
        @Positive
        BigDecimal total,
        @PositiveOrZero
        BigDecimal fees,
        @PositiveOrZero
        BigDecimal taxes
) {}