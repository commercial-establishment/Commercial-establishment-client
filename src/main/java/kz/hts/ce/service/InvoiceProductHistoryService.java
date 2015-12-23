package kz.hts.ce.service;

import kz.hts.ce.model.entity.InvoiceProductHistory;
import kz.hts.ce.repository.InvoiceProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceProductHistoryService extends BaseService<InvoiceProductHistory, InvoiceProductHistoryRepository>{
    @Autowired
    protected InvoiceProductHistoryService(InvoiceProductHistoryRepository repository) {
        super(repository);
    }
}
