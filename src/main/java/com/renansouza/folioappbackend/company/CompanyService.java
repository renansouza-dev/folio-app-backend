package com.renansouza.folioappbackend.company;

import com.renansouza.folioappbackend.company.exceptions.CompanyAlreadyExistsException;
import com.renansouza.folioappbackend.company.exceptions.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    Company getCompanyById(Long id, String cnpj, String ticker) {
        if (id != null) {
            return repository.findById(id)
                    .orElseThrow(CompanyNotFoundException::new);
        } else if (cnpj != null) {
            return repository.findByCnpj(cnpj)
                    .orElseThrow(CompanyNotFoundException::new);
        }

        return repository.findByTicker(ticker)
                .orElseThrow(CompanyNotFoundException::new);
    }

    void createCompany(CompanyRequest companyRequest) {
        if (repository.existsByCnpj(companyRequest.cnpj())) {
            throw new CompanyAlreadyExistsException();
        }

        var company = new Company(companyRequest);
        repository.persist(company);
    }

    void updateCompany(Company company) {
        var savedCompany = repository.findByCnpj(company.getCnpj());
        if (savedCompany.isEmpty()) {
            throw new CompanyAlreadyExistsException();
        }

        company.setId(savedCompany.get().getId());
        repository.merge(company);
    }

    public void deleteCompany(Long id) {
        var company = repository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);

        repository.delete(company);
    }

}