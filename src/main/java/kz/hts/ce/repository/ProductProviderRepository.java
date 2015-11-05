package kz.hts.ce.repository;

import kz.hts.ce.entity.ProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductProviderRepository extends JpaRepository<ProductProvider, Long> {

    List<ProductProvider> findByProvider_Id(long id);

    ProductProvider findByProvider_IdAndProduct_Id(long providerId, long productId);
}