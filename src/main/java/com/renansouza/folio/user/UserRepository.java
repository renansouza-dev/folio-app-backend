package com.renansouza.folio.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByNameIgnoreCase(String name);
}
