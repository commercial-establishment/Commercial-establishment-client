package kz.hts.ce.service;

import kz.hts.ce.model.entity.InvoiceWarehouseProduct;
import kz.hts.ce.repository.InvoiceWarehouseProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceWarehouseProductService extends BaseService<InvoiceWarehouseProduct, InvoiceWarehouseProductRepository> {

    @Autowired
    protected InvoiceWarehouseProductService(InvoiceWarehouseProductRepository repository) {
        super(repository);
    }
}