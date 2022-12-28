package com.renansouza.folio.transaction;

import com.renansouza.folio.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    void deleteById(long id);

    Transaction save(Transaction transaction);

    List<Transaction> findAll();

    Optional<Transaction> findById(long id);

    Optional<List<Transaction>> findByClassificationIgnoreCase(String classification);

}