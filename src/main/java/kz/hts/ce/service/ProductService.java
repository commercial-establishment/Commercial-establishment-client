package kz.hts.ce.service;

import kz.hts.ce.model.entity.Product;
import kz.hts.ce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Product> getHistory(long time) {
        List<Product> allProducts = findAll();
        List<Product> products = new ArrayList<>();
        for (Product productFromAllProducts : allProducts) {
            Revisions<Integer, Product> revisions = repository.findRevisions(productFromAllProducts.getId());
            List<Revision<Integer, Product>> revisionList = revisions.getContent();
            Product product = null;
            for (Revision<Integer, Product> revision : revisionList) {
                long dateTimeInMillis = revision.getMetadata().getRevisionDate().getMillis();
                if (time < dateTimeInMillis) {
                    product = revision.getEntity();
                    product.setCategory(productFromAllProducts.getCategory());/*FIXME*/
                    product.setUnit(productFromAllProducts.getUnit());
                }
            }
            if (product != null) products.add(product);
        }
        return products;
    }

    public void saveOrUpdateList(List<Product> products) {
        products.forEach(this::save);
    }

}
