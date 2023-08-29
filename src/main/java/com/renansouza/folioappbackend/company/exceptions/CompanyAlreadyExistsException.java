package com.renansouza.folioappbackend.company.exceptions;

public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException() {
        super("A company with the provided id already exists.");
    }
}