package com.renansouza.folio.user;

import com.renansouza.folio.shared.EntityAuditorAware;
import com.renansouza.folio.user.exception.UserAlreadyExistsException;
import com.renansouza.folio.user.exception.UserNotFoundException;
import com.renansouza.folio.utils.WordUtils;
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

        var auditor = new EntityAuditorAware().getCurrentAuditor();
        var user = User.builder()
                .name(WordUtils.capitalizeFully(userForm.getName()))
                .avatar(userForm.getAvatar())
                .createdBy(String.valueOf(auditor))
                .lastModifiedBy(String.valueOf(auditor))
                .build();

        return repository.save(user);
    }
}