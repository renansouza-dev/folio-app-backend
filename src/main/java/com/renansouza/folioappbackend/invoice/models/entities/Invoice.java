package com.renansouza.folioappbackend.invoice.models.entities;

import com.renansouza.folioappbackend.invoice.models.InvoiceOperation;
import com.renansouza.folioappbackend.invoice.models.dto.InvoiceRequest;
import com.renansouza.folioappbackend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.renansouza.folioappbackend.invoice.models.InvoiceOperation.BUY;
import static com.renansouza.folioappbackend.invoice.models.InvoiceOperation.SELL;
import static java.math.BigDecimal.ZERO;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Column(nullable = false, length = 10)
    private String broker;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private BigDecimal fees;

    @Column(nullable = false)
    private BigDecimal settlement;

    @Column(nullable = false)
    private BigDecimal net;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InvoiceDetail> details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private User user;

    public Invoice(InvoiceRequest invoiceRequest, User user) {
        var totalAssets = invoiceRequest
                .details()
                .stream()
                .map(detail -> BigDecimal.valueOf(detail.quantity()).multiply(detail.price()))
                .reduce(ZERO, BigDecimal::add);

        this.details = invoiceRequest
                .details()
                .stream()
                .map(detail -> new InvoiceDetail(this, detail, totalAssets, invoiceRequest.settlement()))
                .toList();

        var totalFees = this
                .details
                .stream()
                .map(InvoiceDetail::getFees)
                .reduce(ZERO, BigDecimal::add);

        Map<InvoiceOperation, BigDecimal> operationTotals = this.details
                .stream()
                .filter(detail -> detail.getOperation().equals(BUY) || detail.getOperation().equals(SELL))
                .collect(Collectors.groupingBy(
                        InvoiceDetail::getOperation,
                        Collectors.mapping(invoice -> invoice.getPrice().multiply(BigDecimal.valueOf(invoice.getQuantity())),
                                Collectors.reducing(ZERO, BigDecimal::add)
                        )
                ));

        this.user = user;
        this.fees = totalFees;
        this.total = totalAssets;
        this.date = invoiceRequest.date();
        this.settlement = invoiceRequest.settlement();
        this.broker = invoiceRequest.broker().toUpperCase().trim();
        this.net = operationTotals.getOrDefault(SELL, ZERO)
                .subtract(operationTotals.getOrDefault(BUY, ZERO))
                .subtract(totalFees);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = (o instanceof HibernateProxy proxy) ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = (this instanceof HibernateProxy proxy) ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Invoice invoice = (Invoice) o;
        return getId() != null && Objects.equals(getId(), invoice.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

}