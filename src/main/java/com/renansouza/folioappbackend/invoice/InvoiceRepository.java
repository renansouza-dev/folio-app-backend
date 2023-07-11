package com.renansouza.folioappbackend.invoice;

import com.renansouza.folioappbackend.invoice.models.dto.InvoiceResponse;
import com.renansouza.folioappbackend.invoice.models.entities.Invoice;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends BaseJpaRepository<Invoice, Long> {

    List<InvoiceResponse> findByUserUuid(@Param("uuid") UUID uuid);

    Optional<InvoiceResponse> findInvoiceById(@Param("id") Long id);

}