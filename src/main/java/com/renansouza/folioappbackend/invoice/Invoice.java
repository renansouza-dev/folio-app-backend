package com.renansouza.folioappbackend.invoice;

import com.renansouza.folioappbackend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate date;
    private String broker;
    private BigDecimal total;
    private BigDecimal fees;
    private BigDecimal taxes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private User user;

    public Invoice(InvoiceRequest invoiceRequest, User user) {
        this.user = user;
        this.date = invoiceRequest.date();
        this.fees = invoiceRequest.fees();
        this.total = invoiceRequest.total();
        this.taxes = invoiceRequest.taxes();
        this.broker = invoiceRequest.broker().toUpperCase().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(date, invoice.date) && Objects.equals(broker, invoice.broker) && Objects.equals(total, invoice.total) && Objects.equals(fees, invoice.fees) && Objects.equals(taxes, invoice.taxes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, broker, total, fees, taxes);
    }

}