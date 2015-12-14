package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WarehouseProductHistoryRepository extends JpaRepository<WarehouseProductHistory, Long> {

    List<WarehouseProductHistory> findByDateBetweenAndWarehouseProduct_Product_Id(Date firstDate, Date secondDate, long productId);
    WarehouseProductHistory findByVersion(int version);

    @Query("SELECT wph FROM WarehouseProductHistory wph WHERE wph.date <= ?1 AND wph.warehouseProduct.product.id = ?2 ORDER BY wph.date ASC")
    List<WarehouseProductHistory> findPastNearestDate(Date date, long productId);

    @Query("SELECT wph FROM WarehouseProductHistory wph WHERE wph.date >= ?1 AND wph.warehouseProduct.product.id = ?2 ORDER BY wph.date ASC")
    List<WarehouseProductHistory> findNextNearestDate(Date date, long productId);

}
