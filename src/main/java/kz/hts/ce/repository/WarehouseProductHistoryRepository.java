package kz.hts.ce.repository;

import kz.hts.ce.model.entity.WarehouseProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseProductHistoryRepository extends JpaRepository<WarehouseProductHistory, Long> {

}
