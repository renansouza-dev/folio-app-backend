# Objectives
The main goal of this application is to delete all my spreadsheets about investments and learn/practice new stacks.

# Stacks
Development
* [Java 21](https://openjdk.org/projects/jdk/21/)
* [Spring Boot 3](https://spring.io/projects/spring-boot)

Persistence
* [PostgreSQL](https://www.postgresql.org/)
* [Flyway Migration](https://flywaydb.org/)

Devops
* [GitHub Actions](https://docs.github.com/en/actions)
  - [JaCoCo badge](https://github.com/cicirello/jacoco-badge-generator) Add an action to generate a badge for the code coverage

# Build from source
```shell
docker image build -t renansouza/folio-app-backend .
```

# How to run
```shell
docker compose up -d
```

# Best finds so far
1. [Java 9 modules](https://www.oracle.com/corporate/features/understanding-java-9-modules.html) I could limit
   the number of modules I add, thereby shrinking the final jar file and lowering the risk of potential security glitches.
    ```
   RUN ["jlink", "--compress=2", \
     "--module-path", "/opt/jdk/jdk-21.0.1/jmods/", \
     "--add-modules", "java.base,java.logging,java.naming,java.desktop,jdk.unsupported,java.management,java.security.jgss,java.instrument,java.sql", \
     "--no-header-files", "--no-man-pages", \
     "--output", "/springboot-runtime"]
   ```
2. [Java Records](https://docs.oracle.com/en/java/javase/17/language/records.html) I was able to use records as DTOs
   and minimize the usage of Lombok and its annotations. Furthermore, I am granted immutability for free.

   `public record CompaniesResponse(UUID id, String name, String cnpj, boolean broker, boolean listed) {}`


3. [Java Record as Spring Projection](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html)
   I was able to retrieve the data from the database and map it to a record. This way, I could avoid mapping it from the entity to the DTO.
    