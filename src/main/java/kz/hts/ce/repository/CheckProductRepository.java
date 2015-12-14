package kz.hts.ce.repository;

import kz.hts.ce.model.entity.CheckProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckProductRepository extends JpaRepository<CheckProduct, Long>{
    List<CheckProduct> findByCheck_Id(Long id);
    List<CheckProduct> findByCheck_CheckNumber(String checkNumber);
}
