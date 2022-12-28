package com.renansouza.folio.transaction;

import com.renansouza.folio.shared.EntityAuditorAware;
import com.renansouza.folio.transaction.exception.TransactionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public Transaction add(Transaction transaction) {
        var auditor = new EntityAuditorAware().getCurrentAuditor();

        transaction.setCreatedBy(String.valueOf(auditor));
        transaction.setLastModifiedBy(String.valueOf(auditor));

        return repository.save(transaction);
    }

    Iterable<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction findById(long id) throws TransactionNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> findByClassification(String classification) throws TransactionNotFoundException {
        return repository
                .findByClassificationIgnoreCase(classification)
                .orElseThrow(() -> new TransactionNotFoundException(classification));
    }

}