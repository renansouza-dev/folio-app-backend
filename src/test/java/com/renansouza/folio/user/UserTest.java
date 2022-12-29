package com.renansouza.folio.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class UserTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void nameIsNull() {
        var user = new User(null, null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        Assertions.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void nameIsBlank() {
        var user = new User("", null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        Assertions.assertEquals(2, constraintViolations.size());
    }

    @Test
    public void nameIsShort() {
        var user = new User("abc", null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        Assertions.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void nameIsOk() {
        var user = new User("username", "https://www.example.com");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        Assertions.assertEquals(0, constraintViolations.size());
        Assertions.assertEquals("Username", user.getName());
    }

    @Test
    public void avatarIsNullAndItsOk() {
        var user = new User("username", null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        Assertions.assertEquals(0, constraintViolations.size());
    }

    @Test
    public void avatarIsInvalid() {
        var user = new User("username", "http://www.example.com");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        Assertions.assertEquals(1, constraintViolations.size());
    }
}