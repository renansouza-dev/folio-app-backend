package com.renansouza.folio.transaction;

import com.renansouza.folio.shared.EntityAuditorAware;
import com.renansouza.folio.transaction.exception.TransactionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    void update(Transaction transaction) throws TransactionNotFoundException {
        var savedData = repository
                .findById(transaction.getId())
                .orElseThrow(() -> new TransactionNotFoundException(transaction.getId()));

        // Should validate if the object is really updated?
        savedData.setDate(transaction.getDate());
        savedData.setAmount(transaction.getAmount());
        savedData.setRevenue(transaction.getRevenue());
        savedData.setDueDate(transaction.getDueDate());
        savedData.setQuantity(transaction.getQuantity());
        savedData.setLastModifiedDate(LocalDateTime.now());
        savedData.setClassification(transaction.getClassification());

        repository.save(savedData);
    }

    Transaction add(Transaction transaction) {
        var auditor = new EntityAuditorAware().getCurrentAuditor();

        transaction.setCreatedBy(String.valueOf(auditor));
        transaction.setLastModifiedBy(String.valueOf(auditor));

        return repository.save(transaction);
    }

    Iterable<Transaction> findAll() {
        return repository.findAll();
    }

    Transaction findById(long id) throws TransactionNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    List<Transaction> findByClassification(String classification) throws TransactionNotFoundException {
        return repository
                .findByClassificationIgnoreCase(classification)
                .orElseThrow(() -> new TransactionNotFoundException(classification));
    }

}