package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.companies.exceptions.CompaniesAlreadyExistsException;
import com.renansouza.folioappbackend.companies.exceptions.CompaniesNotFoundException;
import com.renansouza.folioappbackend.companies.models.CompaniesEntity;
import com.renansouza.folioappbackend.companies.models.CompaniesRequest;
import com.renansouza.folioappbackend.companies.models.CompaniesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompaniesService {

    private final CompaniesRepository companiesRepository;

    Page<CompaniesResponse> getCompanies(UUID id, String cnpj, Pageable page) {
        return id == null && cnpj == null
                ? companiesRepository.findAllCompanies(page)
                : companiesRepository.findAllByIdOrCnpj(id, cnpj, page);
    }

    void createCompanies(CompaniesRequest companiesRequest) {
        if (companiesRepository.existsByCnpj(companiesRequest.cnpj())) {
            throw new CompaniesAlreadyExistsException();
        }

        var entity = new CompaniesEntity(companiesRequest);
        companiesRepository.save(entity);
    }

    void updateCompanies(UUID id, CompaniesRequest companiesRequest) {
        var entity = companiesRepository
                .findById(id)
                .orElseThrow(CompaniesNotFoundException::new);

        entity.setName(companiesRequest.name());
        entity.setCnpj(companiesRequest.cnpj());
        entity.setModified(LocalDateTime.now());
        entity.setBroker(companiesRequest.broker());
        entity.setListed(companiesRequest.listed());

        companiesRepository.save(entity);
    }

    void deleteCompany(UUID id) {
        var entity = companiesRepository
                .findById(id)
                .orElseThrow(CompaniesNotFoundException::new);

        companiesRepository.delete(entity);
    }

}