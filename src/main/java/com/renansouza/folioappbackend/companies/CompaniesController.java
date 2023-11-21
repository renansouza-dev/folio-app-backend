package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.companies.models.CompaniesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/companies")
public class CompaniesController {

    private final CompaniesService companiesService;

    @GetMapping
    ResponseEntity<Object> getCompanies(@RequestParam(required = false) UUID id,
                                        @RequestParam(required = false) String cnpj,
                                        @RequestParam(required = false, defaultValue = "20") String pageSize) {
        var companies = companiesService.getCompanies(id, cnpj, pageSize);
        return companies.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(companies);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createCompanies(@RequestBody CompaniesRequest companiesRequest) {
        companiesService.createCompanies(companiesRequest);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateCompanies(@PathVariable UUID id, @RequestBody CompaniesRequest companiesRequest) {
        companiesService.updateCompanies(id, companiesRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompany(@PathVariable UUID id) {
        companiesService.deleteCompany(id);
    }

}