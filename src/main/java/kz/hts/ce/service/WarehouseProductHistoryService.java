package kz.hts.ce.service;

import kz.hts.ce.model.entity.WarehouseProductHistory;
import kz.hts.ce.repository.WarehouseProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseProductHistoryService extends BaseService<WarehouseProductHistory, WarehouseProductHistoryRepository>{

    @Autowired
    protected WarehouseProductHistoryService(WarehouseProductHistoryRepository repository) {
        super(repository);
    }
}
