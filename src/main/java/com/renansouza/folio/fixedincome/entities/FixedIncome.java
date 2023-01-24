package com.renansouza.folio.fixedincome.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "fixed_incomes")
public class FixedIncome implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(min = 5, max = 255)
    private String brokerage;

    @NotBlank
    @Size(min = 5, max = 255)
    private String emitter;

    @Enumerated(EnumType.STRING)
    private Forma forma;

    @Positive
    private BigDecimal amount;

    @PastOrPresent
    private LocalDate date;

    @Future
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private Index index;

    @Positive
    private double rate;

    @PastOrPresent
    private LocalDate withdrawDate;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
