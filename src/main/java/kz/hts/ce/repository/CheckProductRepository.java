package kz.hts.ce.repository;

import kz.hts.ce.model.entity.CheckProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CheckProductRepository extends JpaRepository<CheckProduct, UUID> {

    List<CheckProduct> findByCheck_Id(UUID id);

    List<CheckProduct> findByCheck_CheckNumber(String checkNumber);
}
