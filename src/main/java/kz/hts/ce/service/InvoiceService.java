package kz.hts.ce.service;

import kz.hts.ce.model.entity.Invoice;
import kz.hts.ce.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService extends BaseService<Invoice, InvoiceRepository> {

    @Autowired
    protected InvoiceService(InvoiceRepository repository) {
        super(repository);
    }

    public List<Invoice> findByWarehouseShopId(UUID id) {
        return repository.findByWarehouse_Shop_Id(id);
    }

    public void updatePostponementById(int postponement, UUID id) {
        repository.updatePostponementById(postponement, id);
    }

    public void updateVatById(boolean vat, UUID id) {
        repository.updateVatById(vat, id);
    }

    public List<Invoice> findByWarehouseShopIdAndProviderId(UUID shopId, UUID providerId) {
        return repository.findByWarehouse_Shop_IdAndProvider_Id(shopId, providerId);
    }
}
