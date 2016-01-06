package kz.hts.ce.service;

import kz.hts.ce.model.entity.Warehouse;
import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.repository.WarehouseProductRepository;
import kz.hts.ce.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService extends BaseService<Warehouse, WarehouseRepository> {

    @Autowired
    protected WarehouseService(WarehouseRepository repository) {
        super(repository);
    }

    public Warehouse findByShopId(UUID id) {
        return repository.findByShop_Id(id);
    }
}
