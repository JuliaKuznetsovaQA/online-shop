package com.kuznetsova.online_shop.services;

import com.kuznetsova.online_shop.models.Order;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Order createOrder(Long userId, Map<Long, Integer> productQuantities);
    List<Order> getAllOrders();
}
