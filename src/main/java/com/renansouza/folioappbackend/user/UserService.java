package com.renansouza.folioappbackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    UserRecord findById(UUID uuid) {
        return repository.findUserRecordByUuid(uuid).orElseThrow(UserNotFoundException::new);
    }

    UserRecord findMe(String email) {
        var optionalUser = repository.findByEmail(email);
        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            return new UserRecord(user.getUuid(), user.getName(), user.getEmail(), user.getPicture());
        }

        throw new UserNotFoundException();
    }

    @Transactional
    public void save(User newUser) {
        var savedUser = repository.findByEmail(newUser.getEmail());
        if (savedUser.isPresent()) {
            if (!savedUser.get().equals(newUser)) {
                var updatedUser = savedUser.get();
                updatedUser.setName(newUser.getName());
                updatedUser.setEmail(newUser.getEmail());
                updatedUser.setPicture(newUser.getPicture());

                repository.update(updatedUser);
            }
        } else {
            repository.persist(newUser);
        }
    }

}