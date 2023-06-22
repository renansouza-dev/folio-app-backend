package com.renansouza.folioappbackend.invoice;

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
        var net = invoice.getTotal().subtract(invoice.getFees()).subtract(invoice.getTaxes());

        return InvoiceResponse
                .builder()
                .net(net)
                .id(invoice.getId())
                .date(invoice.getDate())
                .fees(invoice.getFees())
                .total(invoice.getTotal())
                .taxes(invoice.getTaxes())
                .broker(invoice.getBroker())
                .build();
    }

    List<InvoiceResponse> findAllByUserUuid(UUID uuid) {
        return repository.findInvoicesByUserUuid(uuid);
    }

    void update(Long id, InvoiceRequest invoiceRequest) {
        var invoice = repository
                .findById(id)
                .orElseThrow(InvoiceNotFoundException::new);

        invoice.setDate(invoiceRequest.date());
        invoice.setTotal(invoiceRequest.total());
        invoice.setFees(invoiceRequest.fees());
        invoice.setBroker(invoiceRequest.broker());
        invoice.setTaxes(invoiceRequest.taxes());

        repository.merge(invoice);
    }
    void delete(Long id) {
        var invoice = repository
                .findById(id)
                .orElseThrow(InvoiceNotFoundException::new);

        repository.delete(invoice);
    }
}