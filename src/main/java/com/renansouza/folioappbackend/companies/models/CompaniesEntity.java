package com.renansouza.folioappbackend.companies.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies",
       indexes = { @Index(name = "fn_index", columnList = "cnpj") },
       uniqueConstraints = { @UniqueConstraint(name = "UniqueCnpj", columnNames = {"cnpj"})})
public class CompaniesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Size(min = 5, max = 85)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 14, max = 14)
    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private boolean broker;

    @Column(nullable = false)
    private boolean listed;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modified;

    public CompaniesEntity(CompaniesRequest companiesRequest) {
        this.cnpj = companiesRequest.cnpj();
        this.name = companiesRequest.name();
        this.broker = companiesRequest.broker();
        this.listed = companiesRequest.listed();

        var now = LocalDateTime.now();
        this.created = now;
        this.modified = now;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CompaniesEntity that = (CompaniesEntity) o;
        return getUuid() != null && Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}