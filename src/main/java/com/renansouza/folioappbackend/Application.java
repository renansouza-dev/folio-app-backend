package com.renansouza.folioappbackend;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
        value = "com.renansouza.folioappbackend",
        repositoryBaseClass = BaseJpaRepositoryImpl.class
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}