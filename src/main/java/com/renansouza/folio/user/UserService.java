package com.renansouza.folio.user;

import com.renansouza.folio.shared.EntityAuditorAware;
import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public User add(User user) throws UserAlreadyExistsException {
        if (repository.findByNameIgnoreCase(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException(user.getName());
        }

        var auditor = new EntityAuditorAware().getCurrentAuditor();
        user.setCreatedBy(String.valueOf(auditor));
        user.setLastModifiedBy(String.valueOf(auditor));

        return repository.save(user);
    }

    //TODO: Add a role or other solution to prevent account hijack
    public void update(User user) throws UserNotFoundException {
        var oldUserData = repository.findById(user.getId());
        if (oldUserData.isEmpty()) {
            throw new UserNotFoundException(user.getId());
        }

        // Should validate if the object is really updated?
        var newUserData = oldUserData.get();
        newUserData.setName(user.getName());
        newUserData.setAvatar(user.getAvatar());
        newUserData.setLastModifiedDate(LocalDateTime.now());

        repository.save(newUserData);
    }

}