package com.renansouza.folioappbackend.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
record InvoiceResponse(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate date,
        String broker,
        BigDecimal total,
        BigDecimal fees,
        BigDecimal taxes,
        BigDecimal net
) {
}