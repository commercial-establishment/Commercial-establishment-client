package kz.hts.ce.service;

import kz.hts.ce.entity.Employee;
import kz.hts.ce.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends BaseService<Employee, EmployeeRepository>{

    @Autowired
    protected EmployeeService(EmployeeRepository repository) {
        super(repository);
    }

    public Employee findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
