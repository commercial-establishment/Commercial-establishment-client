package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {

    WarehouseProduct findByProduct_Barcode(long barcode);
}
