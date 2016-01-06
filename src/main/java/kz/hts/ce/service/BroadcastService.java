package kz.hts.ce.service;

import kz.hts.ce.model.entity.Broadcast;
import kz.hts.ce.repository.BroadcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BroadcastService extends BaseService<Broadcast, BroadcastRepository> {

    @Autowired
    protected BroadcastService(BroadcastRepository repository) {
        super(repository);
    }

    public List<Broadcast> findByNearestDate(Date date) {
        return repository.findByNearestDate(date);
    }
}
