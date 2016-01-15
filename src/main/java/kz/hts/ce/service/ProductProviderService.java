package kz.hts.ce.service;

import kz.hts.ce.model.entity.ProductProvider;
import kz.hts.ce.repository.ProductProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ProductProvider> findByProductId(UUID productId) {
        return repository.findByProduct_Id(productId);
    }

    public ProductProvider findByProviderIdAndProductId(UUID providerId, UUID productId) {
        return repository.findByProvider_IdAndProduct_Id(providerId, productId);
    }

    public List<ProductProvider> findByProviderIdAndProductCategoryId(UUID providerId, UUID categoryId) {
        return repository.findByProvider_IdAndProduct_Category_Id(providerId, categoryId);
    }

    public List<ProductProvider> getHistory(long time) {
        List<ProductProvider> allProductProviderList = findAll();
        List<ProductProvider> productProviderList = new ArrayList<>();
        for (ProductProvider ppFromAllProductProviderList : allProductProviderList) {
            Revisions<Integer, ProductProvider> revisions = repository.findRevisions(ppFromAllProductProviderList.getId());
            List<Revision<Integer, ProductProvider>> revisionList = revisions.getContent();
            ProductProvider productProvider = null;
            for (Revision<Integer, ProductProvider> revision : revisionList) {
                long dateTimeInMillis = revision.getMetadata().getRevisionDate().getMillis();
                if (time < dateTimeInMillis) {
                    productProvider = revision.getEntity();
                    productProvider.setProduct(ppFromAllProductProviderList.getProduct());
                }
            }
            if (productProvider != null) productProviderList.add(productProvider);
        }
        return productProviderList;
    }
}
