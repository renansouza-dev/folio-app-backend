package com.renansouza.folio.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User save(User user);

    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findByNameIgnoreCase(String name);

}