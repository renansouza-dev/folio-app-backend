package com.renansouza.folioappbackend.invoice;

import com.renansouza.folioappbackend.invoice.models.entities.InvoiceDetail;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDetailsRepository extends BaseJpaRepository<InvoiceDetail, Long> {}