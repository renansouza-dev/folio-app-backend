package com.renansouza.folioappbackend.invoice.models.entities;

import com.renansouza.folioappbackend.invoice.models.InvoiceOperation;
import com.renansouza.folioappbackend.invoice.models.dto.InvoiceDetailRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "invoices_details")
public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6, nullable = false)
    private String asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceOperation operation;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal fees;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    public InvoiceDetail(Invoice invoice, InvoiceDetailRecord invoiceDetailRecord, BigDecimal total, BigDecimal fees) {
        this.invoice = invoice;
        this.asset = invoiceDetailRecord.asset().toUpperCase().trim();
        this.price = invoiceDetailRecord.price();
        this.quantity = invoiceDetailRecord.quantity();
        this.operation = invoiceDetailRecord.operation();

        var totalValue = BigDecimal.valueOf(this.quantity).multiply(this.price);
        var liquidation = totalValue
                .multiply(BigDecimal.valueOf(0.000250))
                .setScale(2, RoundingMode.HALF_UP);
        this.fees = totalValue
                .divide(total, 2, RoundingMode.HALF_UP)
                .multiply(fees)
                .add(liquidation)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDetail that = (InvoiceDetail) o;
        return quantity == that.quantity
                && Objects.equals(asset, that.asset)
                && operation == that.operation
                && Objects.equals(price, that.price)
                && Objects.equals(fees, that.fees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asset, operation, quantity, price, fees);
    }

}