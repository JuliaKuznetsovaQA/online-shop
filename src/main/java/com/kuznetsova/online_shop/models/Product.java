package com.kuznetsova.online_shop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_price", columnList = "price"),
        @Index(name = "idx_product_category", columnList = "category_id"),
        @Index(name = "idx_product_title", columnList = "title")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY) // для демонстрации проблемы N+1
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
