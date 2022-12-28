package com.renansouza.folio.transaction;

import com.renansouza.folio.transaction.exception.TransactionNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    public static final String CLASSIFICATION = "classification";
    @Mock
    TransactionRepository repository;

    @InjectMocks
    TransactionService service;

    @Test
    @DisplayName("should add a transaction")
    void add() {
        Mockito.when(repository.save(Mockito.any(Transaction.class))).thenReturn(getTransactionOutput());

        var transaction = service.add(getTransactionInput());
        assertNotNull(transaction.getCreatedDate());
        assertNotNull(transaction.getLastModifiedDate());
        assertEquals(transaction.getQuantity(), 1);
        assertEquals(transaction.getAmount(), BigDecimal.TEN);
        assertEquals(transaction.getRevenue(), BigDecimal.TEN);
        assertEquals(transaction.getClassification(), CLASSIFICATION);
        assertEquals(transaction.getDueDate(), LocalDate.now().plus(Period.ofDays(1)));
    }

    @Test
    @DisplayName("should find all transactions")
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(getTransactionOutput()));

        var transactions = service.findAll();
        assertTrue(transactions.iterator().hasNext());
        assertEquals(transactions.iterator().next().getAmount(), BigDecimal.TEN);
    }

    @Test
    @DisplayName("should find a transaction by its id")
    void findById() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(getTransactionOutput()));

        var transaction = service.findById(1L);
        assertEquals(transaction.getAmount(), BigDecimal.TEN);
    }

    @Test
    @DisplayName("should not find a transaction by its id")
    void notFindById() {
        long id = 1L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        TransactionNotFoundException thrown = Assertions.assertThrows(TransactionNotFoundException.class, () -> service.findById(id));
        Assertions.assertEquals("Could not found a transaction with id " + id, thrown.getMessage());
    }

    @Test
    @DisplayName("should find a transaction by its classification")
    void findByClassification() {
        Mockito.when(repository.findByClassificationIgnoreCase(Mockito.anyString()))
                .thenReturn(Optional.of(List.of(getTransactionOutput())));

        var transactions = service.findByClassification(CLASSIFICATION);
        assertTrue(transactions.iterator().hasNext());
        assertEquals(transactions.iterator().next().getAmount(), BigDecimal.TEN);
    }

    @Test
    @DisplayName("should not find a transaction by its classification")
    void notFindByClassification() {
        var classification = CLASSIFICATION;
        Mockito.when(repository.findByClassificationIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());

        TransactionNotFoundException thrown = Assertions.assertThrows(
                TransactionNotFoundException.class, () -> service.findByClassification(classification));
        Assertions.assertEquals("Could not found any transaction with classification " + classification, thrown.getMessage());
    }

    private Transaction getTransactionOutput() {
        var transaction = getTransactionInput();
        transaction.setId(1);
        transaction.setCreatedBy("auditor");
        transaction.setLastModifiedBy("auditor");

        return transaction;
    }

    private Transaction getTransactionInput() {
        var date = LocalDate.now();
        return Transaction.builder()
                .date(date)
                .quantity(1.0)
                .amount(BigDecimal.TEN)
                .revenue(BigDecimal.TEN)
                .classification(CLASSIFICATION)
                .dueDate(date.plus(Period.ofDays(1)))
                .build();
    }

}