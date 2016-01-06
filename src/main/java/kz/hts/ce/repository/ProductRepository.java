package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByBarcode(String barcode);

    List<Product> findByCategory_Name(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.blocked = TRUE where p.id = ?1")
    void lockById(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.blocked = FALSE where p.id = ?1")
    void reestablishById(UUID id);
}
