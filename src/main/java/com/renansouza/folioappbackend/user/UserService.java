package com.renansouza.folioappbackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    @Transactional
    User findById(UUID uuid) {
        return repository.findById(uuid).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void save(OidcUser oidcUser) {
        var newUser = new User(oidcUser);
        var savedUser = repository.findByEmail(newUser.getEmail());
        if (savedUser.isPresent()) {
            if (!savedUser.get().equals(newUser)) {
                repository.update(newUser);
            }
        } else {
            repository.persist(newUser);
        }
    }

}