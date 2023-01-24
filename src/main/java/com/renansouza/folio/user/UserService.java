package com.renansouza.folio.user;

import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    List<User> findAll() {
        return repository.findAll();
    }

    User findById(long id) throws UserNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    User add(User user) throws UserAlreadyExistsException {
        if (repository.findByNameIgnoreCase(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException(user.getName());
        }

        return repository.save(user);
    }

    //TODO: Add a role or other solution to prevent account hijack
    void update(User user) throws UserNotFoundException {
        var savedData = repository
                .findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));

        // Should validate if the object is really updated?
        savedData.setName(user.getName());
        savedData.setAvatar(user.getAvatar());

        repository.save(savedData);
    }

}