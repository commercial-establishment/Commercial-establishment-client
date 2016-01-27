package kz.hts.ce.service;

import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShiftService extends BaseService<Shift, ShiftRepository> {

    @Autowired
    protected ShiftService(ShiftRepository repository) {
        super(repository);
    }

    public Shift openNewShift(Employee employee) {
        Shift shift = new Shift();
        shift.setStart(new Date());
        shift.setEmployee(employee);
        return shift;
    }

    public Shift findByLastEndDate() {
        return repository.findByLastEndDate();
    }
}
