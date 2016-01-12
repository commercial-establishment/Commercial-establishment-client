package kz.hts.ce.service;

import kz.hts.ce.model.entity.WarehouseProduct;
import kz.hts.ce.repository.WarehouseProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WarehouseProductService extends BaseService<WarehouseProduct, WarehouseProductRepository> {

    @Autowired
    protected WarehouseProductService(WarehouseProductRepository repository) {
        super(repository);
    }

    public WarehouseProduct findByProductBarcode(String barcode) {
        return repository.findByProduct_Barcode(barcode);
    }

    public WarehouseProduct findByProductId(UUID id) {
        return repository.findByProduct_Id(id);
    }

    public List<WarehouseProduct> findByCategoryIdAndShopId(UUID categoryId, UUID shopId) {
        return repository.findByProduct_Category_IdAndWarehouse_Shop_Id(categoryId, shopId);
    }

    public List<WarehouseProduct> findByCategoryId(UUID categoryId) {
        return repository.findByProduct_Category_Id(categoryId);
    }

    public WarehouseProduct findByWarehouseIdAndProductId(UUID warehouseId, UUID productId) {
        return repository.findByWarehouse_IdAndProduct_Id(warehouseId, productId);
    }

    public List<WarehouseProduct> getHistory(long time) {
        List<WarehouseProduct> allWarehouseProductList = findAll();
        List<WarehouseProduct> warehouseProductList = new ArrayList<>();
        for (WarehouseProduct wpFromAllWarehouseProductList : allWarehouseProductList) {
            Revisions<Integer, WarehouseProduct> revisions = repository.findRevisions(wpFromAllWarehouseProductList.getId());
            List<Revision<Integer, WarehouseProduct>> revisionList = revisions.getContent();
            WarehouseProduct warehouseProduct = null;
            for (Revision<Integer, WarehouseProduct> revision : revisionList) {
                long dateTimeInMillis = revision.getMetadata().getRevisionDate().getMillis();
                if (time < dateTimeInMillis) {
                    warehouseProduct = revision.getEntity();
                }
            }
            if (warehouseProduct != null) warehouseProductList.add(warehouseProduct);
        }
        return warehouseProductList;
    }
}
