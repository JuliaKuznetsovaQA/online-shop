package com.kuznetsova.online_shop.controllers;

import com.kuznetsova.online_shop.repositories.UserRepository;
import com.kuznetsova.online_shop.services.OrderService;
import com.kuznetsova.online_shop.services.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ProductService productService;

    @GetMapping
    public String ordersPage(Model model) {
        fillModel(model);
        model.addAttribute("orderForm", new OrderForm());
        return "orders/index";
    }

    @PostMapping
    public String createOrder(@ModelAttribute("orderForm") OrderForm orderForm, Model model) {
        try {
            orderService.createOrder(orderForm.getUserId(), orderForm.getQuantities());
            return "redirect:/orders?success";
        } catch (ConcurrencyFailureException e) {
            // Сработает при параллельной покупке последнего товара на уровне REPEATABLE_READ
            model.addAttribute("errorMessage",
                    "К сожалению, этот товар закончился. Попробуйте оформить заказ заново.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Сработает, если товара не хватает на складе или корзина пуста
            model.addAttribute("errorMessage", e.getMessage());
        }

        fillModel(model);
        return "orders/index";
    }

    private void fillModel(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("products", productService.searchProducts("", null, null, null));
        model.addAttribute("orders", orderService.getAllOrders());
    }

    @Data
    public static class OrderForm {
        private Long userId;
        private Map<Long, Integer> quantities = new HashMap<>();
    }
}
