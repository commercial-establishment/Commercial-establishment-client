package kz.hts.ce.service;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static kz.hts.ce.util.JavaUtil.checkConnection;
import static kz.hts.ce.util.JavaUtil.getDateFromInternet;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;
import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Service
public class EmployeeService extends BaseService<Employee, EmployeeRepository> {

    @Autowired
    protected EmployeeService(EmployeeRepository repository) {
        super(repository);
    }

    public Employee findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Employee findByUsernameAndBlocked(String username, boolean blocked) {
        return repository.findByUsernameAndBlocked(username, blocked);
    }

    public List<Employee> getHistory(long time) {
        List<Employee> allEmployees = findAll();
        List<Employee> employees = new ArrayList<>();
        for (Employee employeeFromAllEmployees : allEmployees) {
            Revisions<Integer, Employee> revisions = repository.findRevisions(employeeFromAllEmployees.getId());
            List<Revision<Integer, Employee>> revisionList = revisions.getContent();
            Employee employee = null;
            for (Revision<Integer, Employee> revision : revisionList) {
                long dateTimeInMillis = revision.getMetadata().getRevisionDate().getMillis();
                if (time < dateTimeInMillis) {
                    employee = revision.getEntity();
                    employee.setRole(employeeFromAllEmployees.getRole());
                    employee.setShop(employeeFromAllEmployees.getShop());
                }
            }
            if (employee != null) employees.add(employee);
        }
        return employees;
    }

    public void lockById(UUID id) {
        repository.lockById(id);
    }

    public void reestablishById(UUID id) {
        repository.reestablishById(id);
    }
}
