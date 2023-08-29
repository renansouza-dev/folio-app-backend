package com.renansouza.folioappbackend.company;

import com.renansouza.folioappbackend.invoice.models.entities.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "companies", indexes = {
        @Index(name = "fn_index", columnList = "cnpj")
}, uniqueConstraints = {
        @UniqueConstraint(name = "UniqueNameAndCnpj", columnNames = {"name", "cnpj"})
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 30)
    @Column(nullable = false)
    private String name;

    @Size(min = 14, max = 14)
    @Column(nullable = false)
    private String cnpj;

    @Column(name = "tickers", nullable = false)
    private List<String> tickers;

    @Column(nullable = false)
    private boolean broker;

    @Column(nullable = false)
    private boolean listed;

    public Company(CompanyRequest companyRequest) {
        this.name = companyRequest.name();
        this.cnpj = companyRequest.cnpj();
        this.broker = companyRequest.broker();
        this.listed = companyRequest.listed();
        this.tickers = companyRequest.tickers();
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