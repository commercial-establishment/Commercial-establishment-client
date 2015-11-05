package kz.hts.ce.service;

import kz.hts.ce.entity.ProductProvider;
import kz.hts.ce.repository.ProductProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductProviderService extends BaseService<ProductProvider, ProductProviderRepository> {

    @Autowired
    protected ProductProviderService(ProductProviderRepository repository) {
        super(repository);
    }

    public List<ProductProvider> findByProviderId(long providerId) {
        return repository.findByProvider_Id(providerId);
    }

    public ProductProvider findByProviderIdAndProductId(long providerId, long productId) {
        return repository.findByProvider_IdAndProduct_Id(providerId, productId);
    }
}
