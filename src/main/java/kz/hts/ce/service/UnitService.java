package kz.hts.ce.service;

import kz.hts.ce.model.entity.Unit;
import kz.hts.ce.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitService extends BaseService<Unit, UnitRepository> {

    @Autowired
    protected UnitService(UnitRepository repository) {
        super(repository);
    }

    public Unit findByName(String name){
        return repository.findByName(name);
    }
}
