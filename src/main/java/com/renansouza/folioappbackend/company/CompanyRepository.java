package com.renansouza.folioappbackend.company;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends BaseJpaRepository<Company, Long> {

    boolean existsByCnpj(String cnpj);
    Optional<Company> findByCnpj(String cnpj);

    @Query(value = "select * from companies c where upper(:ticker) = any(c.tickers)", nativeQuery = true)
    Optional<Company> findByTicker(@Param("ticker") String ticker);

}