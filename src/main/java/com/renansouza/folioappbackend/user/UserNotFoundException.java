package com.renansouza.folioappbackend.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {

        super("User with provided uuid not found");
    }
}