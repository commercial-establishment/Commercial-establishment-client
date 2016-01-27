package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends RevisionRepository<Employee, UUID, Integer>, JpaRepository<Employee, UUID> {

    Employee findByUsername(String username);

    Employee findByUsernameAndBlocked(String username, boolean blocked);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e set e.blocked = TRUE where e.id = ?1")
    void lockById(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e set e.blocked = FALSE where e.id = ?1")
    void reestablishById(UUID id);
}
