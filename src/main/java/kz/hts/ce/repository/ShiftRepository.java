package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    @Query("SELECT s FROM Shift s WHERE s.start IN (SELECT max(start) FROM Shift)")
    Shift findByLastEndDate();
}
