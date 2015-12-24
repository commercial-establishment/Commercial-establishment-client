package kz.hts.ce.service;

import kz.hts.ce.model.entity.WarehouseProductHistory;
import kz.hts.ce.repository.WarehouseProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WarehouseProductHistoryService extends BaseService<WarehouseProductHistory, WarehouseProductHistoryRepository> {

    @Autowired
    protected WarehouseProductHistoryService(WarehouseProductHistoryRepository repository) {
        super(repository);
    }

    public List<WarehouseProductHistory> findByDatesBetween(Date firstDate, Date secondDate, long productId) {
        return repository.findByDatesBetween(firstDate, secondDate, productId);
    }

    public List<WarehouseProductHistory> findPastNearestAndEqualsDate(Date date, long productId) {
        return repository.findPastNearestAndEqualsDate(date, productId);
    }

    public List<WarehouseProductHistory> findNextNearestDate(Date date, long productId) {
        return repository.findNextNearestDate(date, productId);
    }

    public WarehouseProductHistory findByVersion(int version) {
        return repository.findByVersion(version);
    }
}
