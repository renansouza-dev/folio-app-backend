package com.renansouza.folioappbackend.companies.exceptions;

public class CompaniesAlreadyExistsException extends RuntimeException {
    public CompaniesAlreadyExistsException() {
        super("A company with the provided data already exists.");
    }
}