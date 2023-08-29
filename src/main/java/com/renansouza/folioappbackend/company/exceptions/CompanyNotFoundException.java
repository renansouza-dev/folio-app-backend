package com.renansouza.folioappbackend.company.exceptions;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException() {
        super("No company found with provided id.");
    }
}