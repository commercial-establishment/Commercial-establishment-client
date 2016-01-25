package kz.hts.ce.service;

import kz.hts.ce.model.entity.Transfer;
import kz.hts.ce.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static kz.hts.ce.util.JavaUtil.getDateFromInternet;

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
        transfer.setDate(getDateFromInternet());
        return save(transfer);
    }
}
