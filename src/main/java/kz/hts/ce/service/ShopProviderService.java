package kz.hts.ce.service;

import kz.hts.ce.entity.ShopProvider;
import kz.hts.ce.repository.ShopProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProviderService extends BaseService<ShopProvider, ShopProviderRepository> {

    @Autowired
    protected ShopProviderService(ShopProviderRepository repository) {
        super(repository);
    }

    public List<ShopProvider> findByProviderId(long id) {
       return repository.findByProvider_Id(id);
    }

    public ShopProvider findByProviderIdAndShopId(long providerId, long shopId) {
       return repository.findByProvider_IdAndShop_Id(providerId, shopId);
    }

    public void lockById(long id) {
        repository.lockById(id);
    }

    public void reestablishById(long id) {
        repository.reestablishById(id);
    }
}
