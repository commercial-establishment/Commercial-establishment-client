package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {

    WarehouseProduct findByProduct_Barcode(String barcode);

    List<WarehouseProduct> findByProduct_Category_IdAndWarehouse_Shop_Id(long categoryId, long shopId);
}
