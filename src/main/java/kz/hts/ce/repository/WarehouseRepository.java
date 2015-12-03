package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Warehouse;
import kz.hts.ce.model.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Warehouse findByShop_Id(long id);
}
