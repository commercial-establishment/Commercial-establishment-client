package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WarehouseProductHistoryRepository extends JpaRepository<WarehouseProductHistory, Long> {

    WarehouseProductHistory findByVersion(int version);

    @Query("SELECT wph FROM WarehouseProductHistory wph WHERE wph.warehouseProduct.product.id = ?3 AND wph.date > ?1 AND wph.date <= ?2")
    List<WarehouseProductHistory> findByDatesBetween(Date firstDate, Date secondDate, long productId);

    @Query("SELECT wph FROM WarehouseProductHistory wph WHERE wph.date <= ?1 AND wph.warehouseProduct.product.id = ?2 ORDER BY wph.date ASC")
    List<WarehouseProductHistory> findPastNearestAndEqualsDate(Date date, long productId);

    @Query("SELECT wph FROM WarehouseProductHistory wph WHERE wph.date >= ?1 AND wph.warehouseProduct.product.id = ?2 ORDER BY wph.date ASC")
    List<WarehouseProductHistory> findNextNearestDate(Date date, long productId);

}
