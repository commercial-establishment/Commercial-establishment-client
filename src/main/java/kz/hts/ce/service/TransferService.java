package kz.hts.ce.service;

import kz.hts.ce.model.entity.Transfer;
import kz.hts.ce.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static kz.hts.ce.util.JavaUtil.getFixedDate;

@Service
public class TransferService extends BaseService<Transfer, TransferRepository> {

    @Autowired
    protected TransferService(TransferRepository repository) {
        super(repository);
    }

    public Transfer findByLastDate() {
        return repository.findByLastDate();
    }

    public Transfer saveWithNewDate() {
        Transfer transfer = new Transfer();
        transfer.setDate(new Date());
        return save(transfer);
    }
}
