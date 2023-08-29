package com.renansouza.folioappbackend.invoice.models.entities;

import com.renansouza.folioappbackend.invoice.models.InvoiceOperation;
import com.renansouza.folioappbackend.invoice.models.dto.InvoiceDetailRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

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
    @Column(name = "id", nullable = false)
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = (o instanceof HibernateProxy proxy) ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = (this instanceof HibernateProxy proxy) ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        InvoiceDetail that = (InvoiceDetail) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}