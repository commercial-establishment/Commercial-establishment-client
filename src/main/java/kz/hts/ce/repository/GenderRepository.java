package kz.hts.ce.repository;

import kz.hts.ce.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

    Gender findByName(String name);
}
