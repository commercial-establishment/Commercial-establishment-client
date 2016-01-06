package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findByUsername(String username);

    Employee findByUsernameAndBlocked(String username, boolean blocked);
}
