package kz.hts.ce.repository;

import kz.hts.ce.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByWarehouse_Shop_Id(long id);


    @Transactional
    @Modifying
    @Query("UPDATE Invoice i set i.postponement = ?1 where i.id = ?2")
    void updatePostponementById(int postponement, long id);

    @Transactional
    @Modifying
    @Query("UPDATE Invoice i set i.vat = ?1 where i.id = ?2")
    void updateVatById(boolean vat, long id);

}
