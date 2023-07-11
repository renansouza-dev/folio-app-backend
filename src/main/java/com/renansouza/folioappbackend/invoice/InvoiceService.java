package com.renansouza.folioappbackend.invoice;

import com.renansouza.folioappbackend.invoice.models.dto.InvoiceRequest;
import com.renansouza.folioappbackend.invoice.models.dto.InvoiceResponse;
import com.renansouza.folioappbackend.invoice.models.entities.Invoice;
import com.renansouza.folioappbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository repository;
    private final UserRepository userRepository;

    InvoiceResponse save(InvoiceRequest invoiceRequest) {
        var user = userRepository.getReferenceById(invoiceRequest.uuid_user());
        var invoice = repository.persist(new Invoice(invoiceRequest, user));

        return repository.findInvoiceById(invoice.getId())
                .orElseThrow(InvoiceNotFoundException::new);
    }

    InvoiceResponse findById(Long id) {
        return repository.findInvoiceById(id)
                .orElseThrow(InvoiceNotFoundException::new);
    }

    List<InvoiceResponse> findByUserUuid(UUID uuid) {
        return repository.findByUserUuid(uuid);
    }

    void delete(Long id) {
        var invoice = repository
                .findById(id)
                .orElseThrow(InvoiceNotFoundException::new);

        repository.delete(invoice);
    }

}