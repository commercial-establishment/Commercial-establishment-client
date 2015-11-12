package kz.hts.ce.service;

import kz.hts.ce.model.entity.Gender;
import kz.hts.ce.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService extends BaseService<Gender, GenderRepository> {

    @Autowired
    protected GenderService(GenderRepository repository) {
        super(repository);
    }

    public Gender findByName(String name){
       return repository.findByName(name);
    }
}
