package kz.hts.ce.repository;

import kz.hts.ce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String categoryName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = ?1")
    void delete(long id);
}


