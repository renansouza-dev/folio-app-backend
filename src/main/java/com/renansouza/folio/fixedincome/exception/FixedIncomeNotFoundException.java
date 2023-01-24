package com.renansouza.folio.fixedincome.exception;

public class FixedIncomeNotFoundException extends RuntimeException {

    public FixedIncomeNotFoundException(Long id) {
        super("Could not found an fixed income with id " + id);
    }

}