package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    @Query("SELECT sh FROM Shift sh WHERE ((sh.start > ?1 AND sh.start <= ?2) OR (sh.end > ?1 AND sh.end <= ?2)) AND sh.end IS NOT NULL")
    List<Shift> findByDatesBetween(Date firstDate, Date secondDate);
}
