package kz.hts.ce.service;

import kz.hts.ce.model.entity.Check;
import kz.hts.ce.repository.CheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckService extends BaseService<Check, CheckRepository> {
    @Autowired
    protected CheckService(CheckRepository repository) {
        super(repository);
    }

    public List<Check> findByCheckNumber(String checkNumber){
        return repository.findByCheckNumber(checkNumber);
    }

    public Check findByLastDate(){
        return repository.findByLastDate();
    }
}
