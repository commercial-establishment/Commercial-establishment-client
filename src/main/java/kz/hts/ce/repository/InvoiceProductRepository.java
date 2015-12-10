package kz.hts.ce.repository;

import kz.hts.ce.model.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    List<InvoiceProduct> findByInvoice_Id(long id);

    @Transactional
    @Modifying
    @Query("UPDATE InvoiceProduct p set p.amount = ?1 where p.id = ?2")
    void updateAmountById(int amount, long id);

    @Transactional
    @Modifying
    @Query("UPDATE InvoiceProduct p set p.initialPrice = ?1 where p.id = ?2")
    void updatePriceById(BigDecimal price, long id);
}
