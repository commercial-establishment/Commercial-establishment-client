package kz.hts.ce.service;

import kz.hts.ce.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BaseService<E extends BaseEntity, T extends JpaRepository<E, Long>> {

    protected T repository;

    protected BaseService(T repository) {
        this.repository = repository;
    }

    @Transactional
    public E findById(Long id) {
        return repository.findOne(id);
    }

    @Transactional
    public List<E> findAll() {
        return (List<E>) repository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }

    @Transactional
    public E save(E e) {
        return repository.save(e);
    }
}
