package com.kuznetsova.online_shop.controllers;

import com.kuznetsova.online_shop.models.Product;
import com.kuznetsova.online_shop.repositories.CategoryRepository;
import com.kuznetsova.online_shop.repositories.UserRepository;
import com.kuznetsova.online_shop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String listProducts(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model, Principal principal) {

        List<Product> products;

        if (id != null) {
            products = productService.findById(id).map(List::of).orElse(List.of());
        } else {
            String searchTitle = (title != null) ? title.trim() : "";
            products = productService.searchProducts(searchTitle, categoryId, minPrice, maxPrice);
        }
        if (principal != null) {
            userRepository.findByEmail(principal.getName())
                    .ifPresent(user -> model.addAttribute("currentUser", user));
        }


        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        return "products/index";
    }
}
