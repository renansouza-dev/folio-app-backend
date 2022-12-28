package com.renansouza.folio.shared;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // => a superclass and is not a JPA entity
@NoArgsConstructor // JPA uses the default constructor to create a class using the reflection API
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    @CreatedBy
    public T createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime createdDate = LocalDateTime.now();

    @Setter
    @LastModifiedBy
    public T lastModifiedBy;

    @Setter
    @Getter
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime lastModifiedDate = LocalDateTime.now();

}