package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface WarehouseProductHistoryRepository extends JpaRepository<WarehouseProductHistory, Long> {

    List<WarehouseProductHistory> findByDateBetweenAndWarehouseProduct_Product_Id(Date firstDate, Date secondDate, long productId);

    WarehouseProductHistory findOneByDateLessThanEqual(Date date);
}
