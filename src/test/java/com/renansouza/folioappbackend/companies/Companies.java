package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.companies.models.CompaniesEntity;
import com.renansouza.folioappbackend.companies.models.CompaniesRequest;
import com.renansouza.folioappbackend.companies.models.CompaniesResponse;
import org.instancio.Instancio;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.instancio.Select.field;

class Companies {
    static CompaniesEntity getCompaniesEntity() {
        return Instancio.of(CompaniesEntity.class)
                .set(field(CompaniesEntity::getUuid), UUID.randomUUID())
                .set(field(CompaniesEntity::getName), "Company")
                .set(field(CompaniesEntity::getCnpj), "00000000000100")
                .set(field(CompaniesEntity::isBroker), false)
                .set(field(CompaniesEntity::isListed), true)
                .set(field(CompaniesEntity::getCreated), LocalDateTime.now())
                .set(field(CompaniesEntity::getModified), LocalDateTime.now())
                .create();
    }

    static CompaniesRequest getCompaniesRequest() {
        return Instancio.of(CompaniesRequest.class)
                .set(field(CompaniesRequest::name), "Company")
                .set(field(CompaniesRequest::cnpj), "00000000000100")
                .set(field(CompaniesRequest::broker), false)
                .set(field(CompaniesRequest::listed), true)
                .create();
    }

    static CompaniesResponse getCompaniesResponse() {
        return Instancio.of(CompaniesResponse.class)
                .set(field(CompaniesResponse::id), UUID.randomUUID())
                .set(field(CompaniesResponse::name), "Company")
                .set(field(CompaniesResponse::cnpj), "00000000000100")
                .set(field(CompaniesResponse::broker), false)
                .set(field(CompaniesResponse::listed), true)
                .create();
    }

}