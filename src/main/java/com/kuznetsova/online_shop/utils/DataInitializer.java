package com.kuznetsova.online_shop.utils;

import com.kuznetsova.online_shop.models.Category;
import com.kuznetsova.online_shop.models.Product;
import com.kuznetsova.online_shop.repositories.CategoryRepository;
import com.kuznetsova.online_shop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        // Заполнение данными только если база пустая
        if (categoryRepository.count() == 0) {
            Category electronics = new Category(null, "Электроника");
            Category books = new Category(null, "Книги");
            Category clothes = new Category(null, "Одежда");

            categoryRepository.saveAll(List.of(electronics, books, clothes));

            Product p1 = new Product(null, "Ноутбук MacBook Air", "Тонкий и легкий ноутбук", new BigDecimal("115000.00"), 10, electronics);
            Product p2 = new Product(null, "Смартфон iPhone 15", "Флагманский смартфон", new BigDecimal("85000.00"), 15, electronics);
            Product p3 = new Product(null, "Чистый код (Роберт Мартин)", "Книга по разработке ПО", new BigDecimal("2500.00"), 30, books);
            Product p4 = new Product(null, "Охота на овец", "Харуки Мураками. Охота на овец", new BigDecimal("320.00"), 20, books);
            Product p5 = new Product(null, "Худи оверсайз", "Теплое черное худи", new BigDecimal("4500.00"), 50, clothes);

            productRepository.saveAll(List.of(p1, p2, p3, p4, p5));
            System.out.println("Тестовые категории и товары успешно загружены в БД!");
        }
    }
}
