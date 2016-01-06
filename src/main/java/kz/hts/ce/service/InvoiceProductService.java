package kz.hts.ce.service;

import kz.hts.ce.model.entity.InvoiceProduct;
import kz.hts.ce.repository.InvoiceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceProductService extends BaseService<InvoiceProduct, InvoiceProductRepository> {

    @Autowired
    protected InvoiceProductService(InvoiceProductRepository repository) {
        super(repository);
    }

    public List<InvoiceProduct> findByInvoiceDateBetweenAndProductBarcode(Date start, Date end, String barcode){
        return repository.findByInvoice_DateBetweenAndProduct_Barcode(start, end, barcode);
    }

    public List<InvoiceProduct> findByInvoiceId(UUID invoiceId) {
        return repository.findByInvoice_Id(invoiceId);
    }

    public List<InvoiceProduct> findByProductBarcode(String barcode) {
        return repository.findByProduct_Barcode(barcode);
    }

    public void updatePriceById(BigDecimal price, UUID id){
        repository.updatePriceById(price, id);
    }

    public void updateAmountById(int amount, UUID id){
        repository.updateAmountById(amount, id);
    }
}
