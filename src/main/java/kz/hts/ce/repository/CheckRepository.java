package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Check;
import kz.hts.ce.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface CheckRepository extends JpaRepository<Check, UUID> {

    List<Check> findByCheckNumber(String checkNumber);

    List<Check> findByDateBetween(Date start, Date end);

    List<Check> findByEmployee_Username(String username);

    @Query("SELECT c FROM Check c WHERE c.date IN (SELECT max(date) FROM Check)")
    Check findByLastDate();
}
