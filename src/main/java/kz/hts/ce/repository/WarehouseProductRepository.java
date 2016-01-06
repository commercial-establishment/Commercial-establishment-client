package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, UUID> {

    WarehouseProduct findByProduct_Barcode(String barcode);

    List<WarehouseProduct> findByProduct_Category_IdAndWarehouse_Shop_Id(UUID categoryId, UUID shopId);

    WarehouseProduct findByWarehouse_IdAndProduct_Id(UUID warehouseId, UUID productId);

    List<WarehouseProduct> findByProduct_Category_Id(UUID categoryId);

    WarehouseProduct findByProduct_Id(UUID id);
}
