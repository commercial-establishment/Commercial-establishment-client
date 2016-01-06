package kz.hts.ce.service;

import kz.hts.ce.model.entity.Product;
import kz.hts.ce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService extends BaseService<Product, ProductRepository> {

    @Autowired
    protected ProductService(ProductRepository repository) {
        super(repository);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public void lockById(UUID id) {
        repository.lockById(id);
    }

    public void reestablishById(UUID id) {
        repository.reestablishById(id);
    }

    public Product findByBarcode(String barcode) {
        try {
            return repository.findByBarcode(barcode);
        } catch (ServiceException e) {
            return null;
        }
    }

    public List<Product> findByCategoryName(String name) {
        try {
            return repository.findByCategory_Name(name);
        } catch (ServiceException e) {
            return Collections.emptyList();
        }
    }
}
