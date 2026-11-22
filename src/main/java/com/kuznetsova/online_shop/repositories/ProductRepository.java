package com.kuznetsova.online_shop.repositories;

import com.kuznetsova.online_shop.models.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"category"})
    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Product> searchProducts(@Param("title") String title,
                                 @Param("categoryId") Long categoryId,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);
}
