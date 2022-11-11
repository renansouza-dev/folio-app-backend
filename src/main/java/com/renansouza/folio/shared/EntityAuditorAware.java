package com.renansouza.folio.shared;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class EntityAuditorAware implements AuditorAware<String> {

    // TODO: get real user
    //  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing.interfaces
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("renan");
    }
}
