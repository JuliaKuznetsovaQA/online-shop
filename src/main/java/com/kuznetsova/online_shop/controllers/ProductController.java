package com.kuznetsova.online_shop.controllers;

import com.kuznetsova.online_shop.models.Product;
import com.kuznetsova.online_shop.repositories.CategoryRepository;
import com.kuznetsova.online_shop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public String listProducts(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {

        List<Product> products;

        if (id != null) {
            products = productRepository.findById(id).map(List::of).orElse(List.of());
        } else {
            String searchTitle = (title != null) ? title.trim() : "";
            products = productRepository.searchProducts(searchTitle, categoryId, minPrice, maxPrice);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        return "products/index";
    }
}
