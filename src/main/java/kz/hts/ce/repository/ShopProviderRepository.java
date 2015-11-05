package kz.hts.ce.repository;

import kz.hts.ce.entity.ShopProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ShopProviderRepository extends JpaRepository<ShopProvider, Long> {

    List<ShopProvider> findByProvider_Id(long id);

    ShopProvider findByProvider_IdAndShop_Id(long providerId, long shopId);

    @Transactional
    @Modifying
    @Query("UPDATE ShopProvider sp set sp.blocked = TRUE where sp.id = ?1")
    void lockById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE ShopProvider sp set sp.blocked = FALSE where sp.id = ?1")
    void reestablishById(long id);
}
