package kz.hts.ce.repository;

import kz.hts.ce.model.entity.InvoiceHistory;
import kz.hts.ce.model.entity.WarehouseProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceHistoryRepository extends JpaRepository<InvoiceHistory, Long>{

    List<InvoiceHistory> findByDateBetween(Date firstDate, Date secondDate, long productId);
    InvoiceHistory findByVersion(int version);

}
