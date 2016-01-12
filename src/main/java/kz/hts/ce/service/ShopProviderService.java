package kz.hts.ce.service;

import kz.hts.ce.model.entity.ProductProvider;
import kz.hts.ce.model.entity.Provider;
import kz.hts.ce.model.entity.ShopProvider;
import kz.hts.ce.repository.ShopProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopProviderService extends BaseService<ShopProvider, ShopProviderRepository> {

    @Autowired
    protected ShopProviderService(ShopProviderRepository repository) {
        super(repository);
    }

    public List<ShopProvider> findByProviderId(UUID id) {
       return repository.findByProvider_Id(id);
    }

    public ShopProvider findByProviderIdAndShopId(UUID providerId, UUID shopId) {
       return repository.findByProvider_IdAndShop_Id(providerId, shopId);
    }

    public void lockById(UUID id) {
        repository.lockById(id);
    }

    public void reestablishById(UUID id) {
        repository.reestablishById(id);
    }

    public List<ShopProvider> findByShopId(UUID shopId) {
       return repository.findByShop_Id(shopId);
    }

    public List<Provider> findProvidersByShopId(UUID id) {
        List<ShopProvider> shopProviders = repository.findByShop_Id(id);
        return shopProviders.stream().map(ShopProvider::getProvider).collect(Collectors.toList());
    }

    public List<ShopProvider> getHistory(long time) {
        List<ShopProvider> allShopProviderList = findAll();
        List<ShopProvider> shopProviderList = new ArrayList<>();
        for (ShopProvider spFromAllShopProviderList : allShopProviderList) {
            Revisions<Integer, ShopProvider> revisions = repository.findRevisions(spFromAllShopProviderList.getId());
            List<Revision<Integer, ShopProvider>> revisionList = revisions.getContent();
            ShopProvider shopProvider = null;
            for (Revision<Integer, ShopProvider> revision : revisionList) {
                long dateTimeInMillis = revision.getMetadata().getRevisionDate().getMillis();
                if (time < dateTimeInMillis) {
                    shopProvider = revision.getEntity();
                }
            }
            if (shopProvider != null) shopProviderList.add(shopProvider);
        }
        return shopProviderList;
    }
}
