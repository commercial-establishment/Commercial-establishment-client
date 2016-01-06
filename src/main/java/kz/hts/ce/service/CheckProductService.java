package kz.hts.ce.service;

import kz.hts.ce.model.entity.CheckProduct;
import kz.hts.ce.repository.CheckProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CheckProductService extends BaseService<CheckProduct, CheckProductRepository> {
    @Autowired
    protected CheckProductService(CheckProductRepository repository) {
        super(repository);
    }
    public List<CheckProduct> findByCheckId(UUID id){
        return repository.findByCheck_Id(id);
    }
    public List<CheckProduct> findCheckProductByCheckNumber(String checkNumber){
        return repository.findByCheck_CheckNumber(checkNumber);
    }

}
