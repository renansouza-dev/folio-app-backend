package com.renansouza.folioappbackend.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseJpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<UserRecord> findUserRecordByUuid(@Param("uuid") UUID uuid);

}