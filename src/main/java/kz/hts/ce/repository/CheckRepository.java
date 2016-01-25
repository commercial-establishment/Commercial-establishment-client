package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CheckRepository extends JpaRepository<Check, UUID> {

    List<Check> findByCheckNumber(String checkNumber);

    @Query("SELECT c FROM Check c WHERE c.date IN (SELECT max(date) FROM Check)")
    Check findByLastDate();
}
