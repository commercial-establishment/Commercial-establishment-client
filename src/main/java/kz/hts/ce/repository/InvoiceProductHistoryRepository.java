package kz.hts.ce.repository;

import kz.hts.ce.model.entity.InvoiceProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceProductHistoryRepository extends JpaRepository<InvoiceProductHistory, UUID> {
}
