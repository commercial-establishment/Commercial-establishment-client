package kz.hts.ce.service;

import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.repository.WarehouseProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseProductService extends BaseService<WarehouseProduct, WarehouseProductRepository> {

    @Autowired
    protected WarehouseProductService(WarehouseProductRepository repository) {
        super(repository);
    }

    public WarehouseProduct findByProductBarcode(long barcode) {
        return repository.findByProduct_Barcode(barcode);
    }
}
