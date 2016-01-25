package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT t FROM Transfer  t WHERE t.date IN (SELECT max(date) FROM Transfer)")
    Transfer findByLastDate();
}


