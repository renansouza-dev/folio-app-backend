package com.renansouza.folioappbackend.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public Company getCompany(@RequestParam(required = false) Long id,
                              @RequestParam(required = false) String cnpj,
                              @RequestParam(required = false) String ticker) {
        return companyService.getCompanyById(id, cnpj, ticker);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createCompany(@RequestBody CompanyRequest companyRequest) {
        companyService.createCompany(companyRequest);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateCompany(@RequestBody Company company) {
        companyService.updateCompany(company);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

}