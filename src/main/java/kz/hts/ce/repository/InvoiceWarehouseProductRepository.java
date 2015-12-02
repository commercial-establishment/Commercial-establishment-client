package kz.hts.ce.repository;

import kz.hts.ce.model.entity.InvoiceWarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceWarehouseProductRepository extends JpaRepository<InvoiceWarehouseProduct, Long> {

    List<InvoiceWarehouseProduct> findByWarehouseProduct_Warehouse_Shop_Id(long id);
}