package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.companies.models.CompaniesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                                        @RequestParam(required = false, defaultValue = "20") String pageSize,
                                        @RequestParam(required = false, defaultValue = "name") String property,
                                        @RequestParam(required = false, defaultValue = "asc") String direction) {

        var sort = Sort.by(Sort.Direction.fromString(direction), property);
        var page = PageRequest.of(0, Integer.parseInt(pageSize), sort);

        var companies = companiesService.getCompanies(id, cnpj, page);
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