package kz.hts.ce.service;

import kz.hts.ce.model.entity.Shop;
import kz.hts.ce.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
