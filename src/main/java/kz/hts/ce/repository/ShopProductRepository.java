package kz.hts.ce.repository;

import kz.hts.ce.model.entity.ShopProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {

    ShopProduct findByProduct_Barcode(long barcode);
}
