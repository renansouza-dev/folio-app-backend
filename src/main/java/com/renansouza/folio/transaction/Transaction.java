package com.renansouza.folio.transaction;

import com.renansouza.folio.shared.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends Auditable<String> implements Serializable {

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDate date;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDate dueDate;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal amount;

    @Getter
    @Setter
    @Column(nullable = false)
    private double quantity;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal revenue;

    @Getter
    @Setter
    @Column(nullable = false)
    private String classification;
}
