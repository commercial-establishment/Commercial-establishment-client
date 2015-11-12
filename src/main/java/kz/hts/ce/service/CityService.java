package kz.hts.ce.service;

import kz.hts.ce.model.entity.City;
import kz.hts.ce.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService extends BaseService<City, CityRepository> {

    @Autowired
    protected CityService(CityRepository repository) {
        super(repository);
    }

    public List<City> findAll() {
        return repository.findAll();
    }

    public City findByName(String cityName) {
        return repository.findByName(cityName);
    }
}
