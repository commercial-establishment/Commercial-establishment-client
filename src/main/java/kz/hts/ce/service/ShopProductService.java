package kz.hts.ce.service;

import kz.hts.ce.entity.ShopProduct;
import kz.hts.ce.repository.ShopProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopProductService extends BaseService<ShopProduct, ShopProductRepository> {

    @Autowired
    protected ShopProductService(ShopProductRepository repository) {
        super(repository);
    }

    public ShopProduct findByProductBarcode(long barcode) {
        return repository.findByProduct_Barcode(barcode);
    }
}