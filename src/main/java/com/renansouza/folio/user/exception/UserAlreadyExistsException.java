package com.renansouza.folio.user.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String name) {
        super("An user with the name " + name + " already exists");
    }
}