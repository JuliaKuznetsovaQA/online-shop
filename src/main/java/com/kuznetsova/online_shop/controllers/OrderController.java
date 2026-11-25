package com.kuznetsova.online_shop.controllers;

import com.kuznetsova.online_shop.models.User;
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

import java.security.Principal;
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
    public String ordersPage(Model model, Principal principal) {
        fillModel(model, principal);
        model.addAttribute("orderForm", new OrderForm());
        return "orders/index";
    }

    @PostMapping
    public String createOrder(@ModelAttribute("orderForm") OrderForm orderForm,
                              Principal principal,
                              Model model) {
        try {
            // Получаем текущего залогиненного пользователя из БД по email
            User currentUser = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            orderService.createOrder(currentUser.getId(), orderForm.getQuantities());
            return "redirect:/orders?success";
        } catch (ConcurrencyFailureException e) {
            // Сработает при параллельной покупке последнего товара на уровне REPEATABLE_READ
            model.addAttribute("errorMessage",
                    "К сожалению, этот товар закончился. Попробуйте оформить заказ заново.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Сработает, если товара не хватает на складе или корзина пуста
            model.addAttribute("errorMessage", e.getMessage());
        }

        fillModel(model, principal);
        return "orders/index";
    }

    private void fillModel(Model model, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("products", productService.searchProducts("", null, null, null));
        model.addAttribute("orders", orderService.getAllOrders());
    }

    @Data
    public static class OrderForm {
        private Map<Long, Integer> quantities = new HashMap<>();
    }
}
