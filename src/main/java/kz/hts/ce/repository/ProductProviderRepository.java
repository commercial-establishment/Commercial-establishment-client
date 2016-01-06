package kz.hts.ce.repository;

import kz.hts.ce.model.entity.ProductProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductProviderRepository extends JpaRepository<ProductProvider, UUID> {

    List<ProductProvider> findByProvider_Id(UUID id);

    ProductProvider findByProvider_IdAndProduct_Id(UUID providerId, UUID productId);

    List<ProductProvider> findByProvider_IdAndProduct_Category_Id(UUID providerId, UUID categoryId);
}