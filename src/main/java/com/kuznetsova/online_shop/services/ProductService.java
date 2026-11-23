package com.kuznetsova.online_shop.services;

import com.kuznetsova.online_shop.models.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(Long id);
    List<Product> searchProducts(String title, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice);
}

