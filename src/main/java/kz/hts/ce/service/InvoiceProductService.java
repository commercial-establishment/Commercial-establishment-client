package kz.hts.ce.service;

import kz.hts.ce.model.entity.InvoiceProduct;
import kz.hts.ce.repository.InvoiceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceProductService extends BaseService<InvoiceProduct, InvoiceProductRepository> {

    @Autowired
    protected InvoiceProductService(InvoiceProductRepository repository) {
        super(repository);
    }

    public List<InvoiceProduct> findByInvoiceId(long invoiceId) {
        return repository.findByInvoice_Id(invoiceId);
    }
}
