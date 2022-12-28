package com.renansouza.folio.transaction.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Could not found a transaction with id " + id);
    }

    public TransactionNotFoundException(String classification) {
        super("Could not found any transaction with classification " + classification);
    }
}