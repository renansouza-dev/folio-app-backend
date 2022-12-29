package com.renansouza.folio.user;

import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import com.renansouza.folio.utils.WordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
        var user = new User("User", null);
        List<User> users = Collections.singletonList(getUser());

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
        var user = new User("User", null);

        when(repository.findById(anyLong())).thenReturn(Optional.of(getUser()));
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
    @DisplayName("should add an user")
    void add() {
        var name = "User";
        var user = new User(name, null);

        when(repository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(getUser());

        var expected = service.add(user);
        assertEquals(expected.getName(), user.getName());
    }

    @Test
    @DisplayName("should not add an user")
    void notAdd() {
        var name = "User";
        var user = new User(name, null);

        when(repository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(getUser()));

        var thrown = Assertions.assertThrows(UserAlreadyExistsException.class, () -> service.add(user));
        Assertions.assertEquals("An user with the name " + name + " already exists", thrown.getMessage());
    }

    @Test
    @DisplayName("should update an user")
    void update() {
        var newUserData = getUser();
        newUserData.setName("New User");
        newUserData.setLastModifiedDate(LocalDateTime.now());

        when(repository.findById(anyLong())).thenReturn(Optional.of(getUser()));
        when(repository.save(any(User.class))).thenReturn(newUserData);

        service.update(newUserData);

        Mockito.verify(repository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("should not update an users")
    void notUpdate() {
        var newUserData = getUser();
        newUserData.setName("New User");
        newUserData.setLastModifiedDate(LocalDateTime.now());

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        var thrown = Assertions.assertThrows(UserNotFoundException.class, () -> service.update(newUserData));
        Assertions.assertEquals("Could not found an user with id " + newUserData.getId(), thrown.getMessage());
    }

    private User getUser() {
        var user = new User();
        user.setAvatar("avatar");
        user.setCreatedBy("auditor");
        user.setLastModifiedBy("auditor");
        user.setName(WordUtils.capitalizeFully("User"));

        return user;
    }

}