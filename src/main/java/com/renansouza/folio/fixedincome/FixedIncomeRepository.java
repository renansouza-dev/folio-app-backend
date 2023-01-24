package com.renansouza.folio.fixedincome;

import com.renansouza.folio.fixedincome.entities.FixedIncome;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface FixedIncomeRepository extends PagingAndSortingRepository<FixedIncome, Long> {

    FixedIncome save(FixedIncome fixedIncome);

    List<FixedIncome> findAll();

    Optional<FixedIncome> findById(long id);

}