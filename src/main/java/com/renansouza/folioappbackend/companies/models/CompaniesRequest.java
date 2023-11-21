package com.renansouza.folioappbackend.companies.models;

import jakarta.validation.constraints.NotBlank;

public record CompaniesRequest(@NotBlank
                               String name,
                               @NotBlank
                               String cnpj,
                               boolean broker,
                               boolean listed) {
    public CompaniesRequest {
        name = name.toUpperCase().trim();
        cnpj = cnpj.replaceAll("\\D+", "");
    }

}