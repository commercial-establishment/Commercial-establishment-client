package kz.hts.ce.repository;

import kz.hts.ce.model.entity.InvoiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceHistoryRepository extends JpaRepository<InvoiceHistory, UUID> {

    List<InvoiceHistory> findByDateBetween(Date firstDate, Date secondDate, UUID productId);

    InvoiceHistory findByVersion(int version);

}
