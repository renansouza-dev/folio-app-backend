# Objectives
The main goal of this application is to delete all my spreadsheets about investments and learn/practice new stacks.

## Stacks
### Development
* Java 17
* Spring Boot 3

### Persistence
* PostgreSQL

### Devops
* GitHub Actions
* GraalVM

## How to build the application
```shell
mvn -Pnative spring-boot:build-image
```

## How to run the application
```shell
./folio-app-backend \
    --GOOGLE_CLIENT_ID={GOOGLE_CLIENT_ID} \
    --GOOGLE_CLIENT_SECRET={GOOGLE_CLIENT_SECRET} \
    --REDIRECT_URL_DOMAIN=localhost:8080
```