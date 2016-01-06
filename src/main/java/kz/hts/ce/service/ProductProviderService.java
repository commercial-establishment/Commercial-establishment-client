package kz.hts.ce.service;

import kz.hts.ce.model.entity.ProductProvider;
import kz.hts.ce.repository.ProductProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductProviderService extends BaseService<ProductProvider, ProductProviderRepository> {

    @Autowired
    protected ProductProviderService(ProductProviderRepository repository) {
        super(repository);
    }

    public List<ProductProvider> findByProviderId(UUID providerId) {
        return repository.findByProvider_Id(providerId);
    }

    public ProductProvider findByProviderIdAndProductId(UUID providerId, UUID productId) {
        return repository.findByProvider_IdAndProduct_Id(providerId, productId);
    }

    public List<ProductProvider> findByProviderIdAndProductCategoryId(UUID providerId, UUID categoryId) {
        return repository.findByProvider_IdAndProduct_Category_Id(providerId, categoryId);
    }
}
