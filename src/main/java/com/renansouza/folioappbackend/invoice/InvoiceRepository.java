package com.renansouza.folioappbackend.invoice;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends BaseJpaRepository<Invoice, Long> {

    @Query("""
                select new com.renansouza.folioappbackend.invoice.InvoiceResponse(
                    i.id as id,
                    i.date as date,
                    i.broker as broker,
                    i.total as total,
                    i.fees as fees,
                    i.taxes as taxes,
                    (i.total - i.fees - i.taxes) as net
                )
                from Invoice i
                where i.user.uuid = :uuid
                order by i.id
            """)
    List<InvoiceResponse> findInvoicesByUserUuid(@Param("uuid") UUID uuid);

}