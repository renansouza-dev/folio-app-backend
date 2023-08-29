package com.renansouza.folioappbackend.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CompanyRequest(
        @NotBlank
        @Size(min = 5, max = 30)
        String name,
        @NotBlank
        @Size(min = 14, max = 14)
        String cnpj,
        @NotNull
        List<@NotNull String> tickers,
        boolean broker,
        boolean listed
) {
        public CompanyRequest {
                name = name.toUpperCase();
                cnpj = cnpj.replaceAll("\\D+", "");
                tickers = tickers.stream()
                        .map(String::toUpperCase)
                        .toList();
        }
}