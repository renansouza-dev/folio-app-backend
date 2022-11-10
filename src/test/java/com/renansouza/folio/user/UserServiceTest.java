package com.renansouza.folio.user;

import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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
        var user = new User("User");
        List<User> users = Collections.singletonList(user);

        when(repository.findAll()).thenReturn(users);
        var expected = service.findAll();

        assertTrue(expected.iterator().hasNext());
        assertEquals(expected.iterator().next().getName(), user.getName());
    }

    @Test
    @DisplayName("should not find all users")
    void notFindAll() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        var expected = service.findAll();

        assertFalse(expected.iterator().hasNext());
    }

    @Test
    @DisplayName("should find an user by id")
    void findById() {
        var user = new User("User");

        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        var expected = service.findById(1L);

        assertEquals(expected.getName(), user.getName());
    }

    @Test
    @DisplayName("should not find an user by id")
    void notFindById() {
        long id = 1L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> service.findById(id));
        Assertions.assertEquals("Could not found an user with id " + id, thrown.getMessage());
    }

    @Test
    @DisplayName("should add an users")
    void add() {
        String name = "User";
        var user = new User(name);

        when(repository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(user);

        var expected = service.add(name);
        assertEquals(expected.getName(), user.getName());
    }

    @Test
    @DisplayName("should not add an users")
    void notAdd() {
        String name = "User";
        var user = new User(name);

        when(repository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(user));

        UserAlreadyExistsException thrown = Assertions.assertThrows(UserAlreadyExistsException.class, () -> service.add(name));
        Assertions.assertEquals("An user with the name " + name + " already exists", thrown.getMessage());
    }
}