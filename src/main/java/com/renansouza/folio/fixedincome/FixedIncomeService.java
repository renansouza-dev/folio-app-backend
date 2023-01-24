package com.renansouza.folio.fixedincome;

import com.renansouza.folio.fixedincome.entities.FixedIncome;
import com.renansouza.folio.fixedincome.exception.FixedIncomeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedIncomeService {

    private final FixedIncomeRepository repository;

    List<FixedIncome> findAll() {
        return repository.findAll();
    }

    FixedIncome findById(long id) throws FixedIncomeNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new FixedIncomeNotFoundException(id));
    }

    FixedIncome add(FixedIncome fixedIncome) {
        return repository.save(fixedIncome);
    }

}