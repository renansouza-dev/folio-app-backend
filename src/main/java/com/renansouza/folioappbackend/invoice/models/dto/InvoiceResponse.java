package com.renansouza.folioappbackend.invoice.models.dto;

import com.renansouza.folioappbackend.invoice.models.InvoiceOperation;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceResponse {
    Long getId();
    LocalDate getDate();
    String getBroker();
    BigDecimal getTotal();
    BigDecimal getFees();
    BigDecimal getSettlement();
    BigDecimal getNet();
    List<InvoiceDetailResponse> getDetails();
    interface InvoiceDetailResponse {
        Long getId();
        String getAsset();
        InvoiceOperation getOperation();
        int getQuantity();
        BigDecimal getPrice();
        BigDecimal getFees();
        @Value("#{target.getQuantity() * target.getPrice() - target.getFees()}")
        BigDecimal getNet();
    }

}