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

    public List<WarehouseProductHistory> findByDateBetweenAndProductId(Date firstDate, Date secondDate, long productId) {
        return repository.findByDateBetweenAndWarehouseProduct_Product_Id(firstDate, secondDate, productId);
    }
    public WarehouseProductHistory findFirst1ByDateLessThanEqual(Date date){
        return repository.findOneByDateLessThanEqual(date);
    }
}
