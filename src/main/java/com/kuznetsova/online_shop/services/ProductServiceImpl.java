package com.kuznetsova.online_shop.services;

import com.kuznetsova.online_shop.models.Product;
import com.kuznetsova.online_shop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> searchProducts(String title, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.searchProducts(title, categoryId, minPrice, maxPrice);
    }
}

