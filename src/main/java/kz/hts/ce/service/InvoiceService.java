package kz.hts.ce.service;

import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService extends BaseService<Invoice, InvoiceRepository> {

    @Autowired
    protected InvoiceService(InvoiceRepository repository) {
        super(repository);
    }
}
