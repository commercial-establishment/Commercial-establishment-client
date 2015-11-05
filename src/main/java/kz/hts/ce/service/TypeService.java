package kz.hts.ce.service;

import kz.hts.ce.entity.Type;
import kz.hts.ce.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService extends BaseService<Type, TypeRepository> {
    @Autowired
    protected TypeService(TypeRepository repository) {
        super(repository);
    }

    public List<Type> findAll() {
        return repository.findAll();
    }

    public Type findByName(String name) {
        return repository.findByName(name);
    }
}
