package kz.hts.ce.service;

import kz.hts.ce.model.entity.Shop;
import kz.hts.ce.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ShopService extends BaseService<Shop, ShopRepository> {

    @Autowired
    protected ShopService(ShopRepository repository) {
        super(repository);
    }

    public List<Shop> findAll() {
        return repository.findAll();
    }

    public void lockById(UUID id) {
        repository.lockById(id);
    }

    public void reestablishById(UUID id) {
        repository.reestablishById(id);
    }

    public List<Shop> getHistory(long time) {
        List<Shop> allShops = findAll();
        List<Shop> shops = new ArrayList<>();
        for (Shop shop : allShops) {
            Revisions<Integer, Shop> revisions = repository.findRevisions(shop.getId());
            List<Revision<Integer, Shop>> revisionList = revisions.getContent();
            Shop shopEntity = null;
            for (Revision<Integer, Shop> revision : revisionList) {
                long dateTimeInMillis = revision.getMetadata().getRevisionDate().getMillis();
                if (time < dateTimeInMillis) {
                    shopEntity = revision.getEntity();
                }
            }
            if (shopEntity != null) shops.add(shopEntity);
        }
        return shops;
    }
}
