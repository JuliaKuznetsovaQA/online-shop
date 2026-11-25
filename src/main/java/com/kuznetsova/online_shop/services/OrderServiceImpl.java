package com.kuznetsova.online_shop.services;

import com.kuznetsova.online_shop.models.Order;
import com.kuznetsova.online_shop.models.OrderItem;
import com.kuznetsova.online_shop.models.Product;
import com.kuznetsova.online_shop.models.User;
import com.kuznetsova.online_shop.observers.OrderObserver;
import com.kuznetsova.online_shop.repositories.OrderRepository;
import com.kuznetsova.online_shop.repositories.ProductRepository;
import com.kuznetsova.online_shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final List<OrderObserver> observers;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Order createOrder(Long userId, Map<Long, Integer> productQuantities) {
        if (productQuantities == null || productQuantities.isEmpty()) {
            throw new IllegalArgumentException("Корзина пуста! Выберите хотя бы один товар.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Order order = new Order();
        order.setUser(user);
        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        // Проходим по всем выбранным товарам в рамках ОДНОЙ транзакции
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            if (quantity == null || quantity <= 0) continue;

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + productId + " не найден"));

            if (product.getStockQuantity() < quantity) {
                // Если хотя бы одного товара не хватает — откатится ВЕСЬ заказ и списания предыдущих товаров!
                throw new IllegalStateException("Недостаточно товара '" + product.getTitle() + "' на складе!");
            }

            // Списываем остаток
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);

            // Создаём позицию в чеке
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());
            order.addItem(item);

            totalOrderPrice = totalOrderPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Укажите количество хотя бы для одного товара.");
        }

        order.setTotalPrice(totalOrderPrice);
        Order savedOrder = orderRepository.save(order);

        // Оповещаем Наблюдателей
        observers.forEach(observer -> observer.onOrderCreated(savedOrder));

        return savedOrder;
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

}
