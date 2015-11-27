package kz.hts.ce.repository;

import kz.hts.ce.model.entity.InvoiceWarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceWarehouseProductRepository extends JpaRepository<InvoiceWarehouseProduct, Long> {

}