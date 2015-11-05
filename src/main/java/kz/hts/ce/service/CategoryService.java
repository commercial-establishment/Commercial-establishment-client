package kz.hts.ce.service;

import kz.hts.ce.entity.Category;
import kz.hts.ce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends BaseService<Category, CategoryRepository>{

    @Autowired
    protected CategoryService(CategoryRepository repository) {
        super(repository);
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findByName(String categoryName) {
        return repository.findByName(categoryName);
    }

    public void delete(long id){ repository.delete(id); }
}
