package com.renansouza.folio.user;

import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService service;

    @Test
    @DisplayName("should find all users")
    void findAll() {
        var users = Instancio.ofList(User.class).create();

        when(repository.findAll()).thenReturn(users);
        var expected = service.findAll();

        assertNotNull(expected);
        assertFalse(expected.isEmpty());
    }

    @Test
    @DisplayName("should find an user by id")
    void findById() {
        var user = Instancio.of(User.class).create();

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        var expected = service.findById(1L);

        assertEquals(expected.getName(), user.getName());
    }

    @Test
    @DisplayName("should not find an user by id")
    void notFindById() {
        long id = 1L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var thrown = Assertions.assertThrows(UserNotFoundException.class, () -> service.findById(id));
        Assertions.assertEquals("Could not found an user with id " + id, thrown.getMessage());
    }

    @Test
    @DisplayName("should add an user")
    void add() {
        var user = Instancio.of(User.class).create();

        when(repository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(user);

        var expected = service.add(user);
        assertEquals(expected.getName(), user.getName());
    }

    @Test
    @DisplayName("should not add an user")
    void notAdd() {
        var user = Instancio.of(User.class).create();

        when(repository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(user));

        var thrown = Assertions.assertThrows(UserAlreadyExistsException.class, () -> service.add(user));
        Assertions.assertEquals("An user with the name " + user.getName() + " already exists", thrown.getMessage());
    }

    @Test
    @DisplayName("should update an user")
    void update() {
        var user = Instancio.of(User.class).create();
        user.setName("New User");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);

        service.update(user);

        Mockito.verify(repository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("should not update an users")
    void notUpdate() {
        var user = Instancio.of(User.class).create();

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var thrown = Assertions.assertThrows(UserNotFoundException.class, () -> service.update(user));
        Assertions.assertEquals("Could not found an user with id " + user.getId(), thrown.getMessage());
    }

}