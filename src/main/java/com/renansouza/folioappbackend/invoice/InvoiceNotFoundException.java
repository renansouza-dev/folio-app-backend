package com.renansouza.folioappbackend.invoice;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException() {
        super("Invoice with provided id not found");
    }

}