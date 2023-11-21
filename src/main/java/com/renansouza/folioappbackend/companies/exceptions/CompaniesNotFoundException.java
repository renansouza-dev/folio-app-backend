package com.renansouza.folioappbackend.companies.exceptions;

public class CompaniesNotFoundException extends RuntimeException {
    public CompaniesNotFoundException() {
        super("No company found with provided id.");
    }
}