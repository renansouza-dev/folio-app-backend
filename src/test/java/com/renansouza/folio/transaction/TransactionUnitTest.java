package com.renansouza.folio.transaction;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.stream.Stream;

class TransactionUnitTest {

    private static final Period ONE_DAY = Period.ofDays(1);
    private static final LocalDate TODAY = LocalDate.now();
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private static Stream<Arguments> provideTransactionDateValues() {
        return Stream.of(
                Arguments.of(TODAY, 0),
                Arguments.of(TODAY.minus(ONE_DAY), 0),
                Arguments.of(TODAY.plus(ONE_DAY), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionDateValues")
    public void transactionDate(LocalDate date, int violations) {
        var transaction = Transaction.builder()
                .date(date)
                .dueDate(TODAY.plus(ONE_DAY))
                .amount(BigDecimal.ONE)
                .quantity(1)
                .revenue(BigDecimal.ONE)
                .classification("classification")
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        Assertions.assertEquals(violations, constraintViolations.size());
    }

    private static Stream<Arguments> provideTransactionDueDateValues() {
        return Stream.of(
                Arguments.of(TODAY, 1),
                Arguments.of(TODAY.minus(ONE_DAY), 1),
                Arguments.of(TODAY.plus(ONE_DAY), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionDueDateValues")
    public void transactionDueDate(LocalDate date, int violations) {
        var transaction = Transaction.builder()
                .date(LocalDate.now())
                .dueDate(date)
                .amount(BigDecimal.ONE)
                .quantity(1)
                .revenue(BigDecimal.ONE)
                .classification("classification")
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        Assertions.assertEquals(violations, constraintViolations.size());
    }

    private static Stream<Arguments> provideTransactionAmount() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO, 1),
                Arguments.of(new BigDecimal("-1.0"), 1),
                Arguments.of(BigDecimal.ONE, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionAmount")
    public void transactionAmount(BigDecimal amount, int violations) {
        var transaction = Transaction.builder()
                .date(TODAY)
                .dueDate(TODAY.plus(ONE_DAY))
                .amount(amount)
                .quantity(1)
                .revenue(BigDecimal.ONE)
                .classification("classification")
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        Assertions.assertEquals(violations, constraintViolations.size());
    }

    private static Stream<Arguments> provideTransactionQuantity() {
        return Stream.of(
                Arguments.of(0, 1),
                Arguments.of(-1, 1),
                Arguments.of(1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionQuantity")
    public void transactionQuantity(double quantity, int violations) {
        var transaction = Transaction.builder()
                .date(TODAY)
                .dueDate(TODAY.plus(ONE_DAY))
                .amount(BigDecimal.ONE)
                .quantity(quantity)
                .revenue(BigDecimal.ONE)
                .classification("classification")
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        Assertions.assertEquals(violations, constraintViolations.size());
    }

    private static Stream<Arguments> provideTransactionClassification() {
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("", 1),
                Arguments.of("classification", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionClassification")
    public void transactionClassification(String classification, int violations) {
        var transaction = Transaction.builder()
                .date(TODAY)
                .dueDate(TODAY.plus(ONE_DAY))
                .amount(BigDecimal.ONE)
                .quantity(1)
                .revenue(BigDecimal.ONE)
                .classification(classification)
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        Assertions.assertEquals(violations, constraintViolations.size());
    }
}