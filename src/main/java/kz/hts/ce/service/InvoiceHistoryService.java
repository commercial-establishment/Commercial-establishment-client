package kz.hts.ce.service;

import kz.hts.ce.model.entity.InvoiceHistory;
import kz.hts.ce.repository.InvoiceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceHistoryService extends BaseService<InvoiceHistory, InvoiceHistoryRepository> {
    @Autowired
    protected InvoiceHistoryService(InvoiceHistoryRepository repository) {
        super(repository);
    }
}
