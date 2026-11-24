package com.kuznetsova.online_shop.observers;

import com.kuznetsova.online_shop.models.Order;
import org.springframework.stereotype.Component;

@Component
public class AuditLogObserver implements OrderObserver {
    @Override
    public void onOrderCreated(Order order) {
        System.out.println("[OBSERVER - AUDIT] Зафиксирован заказ #" + order.getId() +
                ": пользователь '" + order.getUser().getName() +
                "', позиций в чеке: " + order.getItems().size() +
                ", итого: " + order.getTotalPrice() + " руб.");
    }
}

