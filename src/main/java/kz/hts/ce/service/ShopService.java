package kz.hts.ce.service;

import kz.hts.ce.entity.Shop;
import kz.hts.ce.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService extends BaseService<Shop, ShopRepository> {

    @Autowired
    protected ShopService(ShopRepository repository) {
        super(repository);
    }

    public List<Shop> findAll() {
        return repository.findAll();
    }

    public void lockById(long id) {
        repository.lockById(id);
    }

    public void reestablishById(long id) {
        repository.reestablishById(id);
    }
}
