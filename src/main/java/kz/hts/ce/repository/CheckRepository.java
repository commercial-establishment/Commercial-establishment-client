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

    List<Check> findByEmployee_Id(UUID uuid);

//    List<Check> findByDateBetweenAndEmployee_Id(Date start, Date end, UUID uuid);

    @Query("SELECT ch FROM Check ch WHERE ch.date > ?1 AND ch.date <= ?2 AND ch.employee.id = ?3")
    List<Check> findByDatesBetweenAndEmployeeId(Date firstDate, Date secondDate, UUID uuid);

    @Query("SELECT c FROM Check c WHERE c.date IN (SELECT max(date) FROM Check)")
    Check findByLastDate();
}
