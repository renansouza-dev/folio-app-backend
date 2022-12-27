package com.renansouza.folio.shared;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // => a superclass and is not a JPA entity
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreatedBy
    public T createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    public LocalDateTime createdDate;

    @LastModifiedBy
    public T lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    public LocalDateTime lastModifiedDate;
}
