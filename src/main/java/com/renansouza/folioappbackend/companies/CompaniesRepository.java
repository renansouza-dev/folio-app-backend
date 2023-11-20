package com.renansouza.folioappbackend.companies;

import com.renansouza.folioappbackend.companies.models.CompaniesEntity;
import com.renansouza.folioappbackend.companies.models.CompaniesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompaniesRepository extends
        ListCrudRepository<CompaniesEntity, UUID>,
        PagingAndSortingRepository<CompaniesEntity, UUID> {

    boolean existsByCnpj(@Param("cnpj") String cnpj);
    @Query(
        """
        SELECT new com.renansouza.folioappbackend.companies.models.CompaniesResponse(c.id, c.name, c.cnpj, c.broker, c.listed)
        FROM CompaniesEntity c
        WHERE c.id = :id OR c.cnpj = :cnpj
        """
    )
    Page<CompaniesResponse> findAllByIdOrCnpj(UUID id, String cnpj, Pageable pageable);

    @Query(
            """
            SELECT new com.renansouza.folioappbackend.companies.models.CompaniesResponse(c.id, c.name, c.cnpj, c.broker, c.listed)
            FROM CompaniesEntity c
            """
    )
    Page<CompaniesResponse> findAllCompanies(Pageable pageable);
}