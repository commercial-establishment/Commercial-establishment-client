package kz.hts.ce.repository;

import kz.hts.ce.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Employee findByUsername(String username);
}
