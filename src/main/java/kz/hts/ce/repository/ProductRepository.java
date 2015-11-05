package kz.hts.ce.repository;

import kz.hts.ce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.blocked = TRUE where p.id = ?1")
    void lockById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE Product p set p.blocked = FALSE where p.id = ?1")
    void reestablishById(long id);
}
