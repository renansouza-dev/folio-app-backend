package com.renansouza.folio.user;

import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    Iterable<User> findAll() {
        return repository.findAll();
    }

    public User findById(long id) throws UserNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User add(UserForm userForm) throws UserAlreadyExistsException {
        if (repository.findByNameIgnoreCase(userForm.getName()).isPresent()) {
            throw new UserAlreadyExistsException(userForm.getName());
        }

        return repository.save(new User(userForm));
    }
}