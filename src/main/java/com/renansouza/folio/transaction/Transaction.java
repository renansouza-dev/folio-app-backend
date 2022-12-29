package com.renansouza.folio.transaction;

import com.renansouza.folio.shared.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends Auditable<String> implements Serializable {

    @Getter
    @PastOrPresent
    @Column(nullable = false)
    private LocalDate date;

    @Getter
    @Future
    @Column(nullable = false)
    private LocalDate dueDate;

    @Getter
    @Positive
    @Column(nullable = false)
    private BigDecimal amount;

    @Getter
    @Positive
    @Column(nullable = false)
    private double quantity;

    @Getter
    @Column(nullable = false)
    private BigDecimal revenue;

    @Getter
    @NotBlank
    @Column(nullable = false)
    private String classification;

}