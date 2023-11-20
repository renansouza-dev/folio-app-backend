package com.renansouza.folioappbackend.companies.models;

import java.util.UUID;

public record CompaniesResponse(UUID id, String name, String cnpj, boolean broker, boolean listed) {}