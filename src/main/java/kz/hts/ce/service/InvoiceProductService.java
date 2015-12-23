package kz.hts.ce.service;

import kz.hts.ce.model.entity.InvoiceProduct;
import kz.hts.ce.repository.InvoiceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceProductService extends BaseService<InvoiceProduct, InvoiceProductRepository> {

    @Autowired
    protected InvoiceProductService(InvoiceProductRepository repository) {
        super(repository);
    }

    public List<InvoiceProduct> findByInvoiceDateBetweenAndProductBarcode(Date start, Date end, String barcode){
        return repository.findByInvoice_DateBetweenAndProduct_Barcode(start, end, barcode);
    }

    public List<InvoiceProduct> findByInvoiceId(long invoiceId) {
        return repository.findByInvoice_Id(invoiceId);
    }

    public List<InvoiceProduct> findByProductBarcode(String barcode) {
        return repository.findByProduct_Barcode(barcode);
    }

    public void updatePriceById(BigDecimal price, long id){
        repository.updatePriceById(price, id);
    }

    public void updateAmountById(int amount, long id){
        repository.updateAmountById(amount, id);
    }
}
