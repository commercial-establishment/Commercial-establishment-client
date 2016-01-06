package kz.hts.ce.service;

import kz.hts.ce.model.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class BaseService<E extends BaseEntity, T extends JpaRepository<E, UUID>> {

    protected T repository;

    protected BaseService(T repository) {
        this.repository = repository;
    }

    @Transactional
    public E findById(UUID id) {
        return repository.findOne(id);
    }

    @Transactional
    public List<E> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(id);
    }

    @Transactional
    public E save(E e) {
        return repository.save(e);
    }
}
