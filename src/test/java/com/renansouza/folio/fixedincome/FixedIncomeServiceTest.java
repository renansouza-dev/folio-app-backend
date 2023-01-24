package com.renansouza.folio.fixedincome;

import com.renansouza.folio.fixedincome.entities.Currency;
import com.renansouza.folio.fixedincome.entities.FixedIncome;
import com.renansouza.folio.fixedincome.entities.Forma;
import com.renansouza.folio.fixedincome.entities.Index;
import com.renansouza.folio.fixedincome.exception.FixedIncomeNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.instancio.Select.all;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FixedIncomeServiceTest {

    @Mock
    FixedIncomeRepository repository;

    @InjectMocks
    FixedIncomeService service;

    @Test
    @DisplayName("should find all fixed incomes")
    void findAll() {
        var fixedIncomes = Instancio.ofList(FixedIncome.class).create();
        when(repository.findAll()).thenReturn(fixedIncomes);

        var expected = service.findAll();

        assertNotNull(expected);
        assertFalse(expected.isEmpty());
    }

    @Test
    @DisplayName("should find an fixed income by it's id")
    void findById() {
        var now = LocalDate.now();
        var fixedIncome = Instancio.of(FixedIncome.class)
                .set(field("brokerage"), "Bank")
                .set(field("emitter"), "Bank")
                .set(all(Forma.class), Forma.POS_FIXADO)
                .set(field( "amount"), BigDecimal.TEN)
                .set(field( "date"), now)
                .set(field( "dueDate"), now.plusYears(3))
                .set(all(Index.class), Index.CDI)
                .set(field("rate"), 1)
                .set(field( "withdrawDate"), now.plusDays(1))
                .set(all(Currency.class), Currency.BRL)
                .create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(fixedIncome));

        var expected = service.findById(fixedIncome.getId());

        assertNotNull(expected);
        assertEquals(expected.getId(), fixedIncome.getId());
        assertEquals(expected.getBrokerage(), "Bank");
        assertEquals(expected.getEmitter(), "Bank");
        assertEquals(expected.getForma(), Forma.POS_FIXADO);
        assertEquals(expected.getAmount(), BigDecimal.TEN);
        assertEquals(expected.getDate(), now);
        assertEquals(expected.getDueDate(), now.plusYears(3));
        assertEquals(expected.getIndex(), Index.CDI);
        assertEquals(expected.getRate(), 1);
        assertEquals(expected.getWithdrawDate(), now.plusDays(1));
        assertEquals(expected.getCurrency(), Currency.BRL);
    }

    @Test
    @DisplayName("should not find an fixed income by it's id")
    void notFindById() {
        long id = 1L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var thrown = Assertions.assertThrows(FixedIncomeNotFoundException.class, () -> service.findById(id));
        Assertions.assertEquals("Could not found an fixed income with id " + id, thrown.getMessage());
    }

    @Test
    @DisplayName("should find an fixed income by it's id")
    void add() {
        var fixedIncome = Instancio.of(FixedIncome.class).create();
        when(repository.save(any(FixedIncome.class))).thenReturn(fixedIncome);

        var expected = service.add(fixedIncome);

        assertNotNull(expected);
        assertEquals(expected.getId(), fixedIncome.getId());
    }

}