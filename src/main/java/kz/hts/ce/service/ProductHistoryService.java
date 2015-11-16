package kz.hts.ce.service;

import kz.hts.ce.model.entity.ProductHistory;
import kz.hts.ce.repository.ProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductHistoryService extends BaseService<ProductHistory, ProductHistoryRepository>{

    @Autowired
    protected ProductHistoryService(ProductHistoryRepository repository) {
        super(repository);
    }
}
