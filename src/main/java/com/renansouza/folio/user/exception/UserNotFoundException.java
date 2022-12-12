package com.renansouza.folio.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not found an user with id " + id);
    }
}