package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends RevisionRepository<Employee, UUID, Integer>, JpaRepository<Employee, UUID> {

    Employee findByUsername(String username);

    Employee findByUsernameAndBlocked(String username, boolean blocked);
}
