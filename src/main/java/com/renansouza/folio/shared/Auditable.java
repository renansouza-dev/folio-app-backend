package com.renansouza.folio.shared;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass // => a superclass and is not a JPA entity
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    @CreatedBy
    public T createdBy;

    @Setter
    @CreatedDate
    @Builder.Default
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime createdDate = LocalDateTime.now();

    @Setter
    @LastModifiedBy
    public T lastModifiedBy;

    @Setter
    @Builder.Default
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime lastModifiedDate = LocalDateTime.now();
}
