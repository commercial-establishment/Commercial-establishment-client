package kz.hts.ce.service;

import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService extends BaseService<Shift, ShiftRepository> {

    @Autowired
    protected ShiftService(ShiftRepository repository) {
        super(repository);
    }
}
