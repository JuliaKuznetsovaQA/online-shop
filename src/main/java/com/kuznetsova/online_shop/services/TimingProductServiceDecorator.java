package com.kuznetsova.online_shop.services;

import com.kuznetsova.online_shop.models.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class TimingProductServiceDecorator implements ProductService {

    private final ProductService delegate;

    public TimingProductServiceDecorator(@Qualifier("productServiceImpl") ProductService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<Product> findById(Long id) {
        long start = System.nanoTime();
        Optional<Product> result = delegate.findById(id);
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("[DECORATOR LOG] Поиск по ID занял: " + durationMs + " мс");
        return result;
    }

    @Override
    public List<Product> searchProducts(String title, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        long start = System.nanoTime();
        List<Product> result = delegate.searchProducts(title, categoryId, minPrice, maxPrice);
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("[DECORATOR LOG] Комбинированный поиск (" + result.size() + " шт.) занял: " + durationMs + " мс");
        return result;
    }
}

