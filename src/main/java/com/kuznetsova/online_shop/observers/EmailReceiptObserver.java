package com.kuznetsova.online_shop.observers;

import com.kuznetsova.online_shop.models.Order;
import org.springframework.stereotype.Component;

@Component
public class EmailReceiptObserver implements OrderObserver {
    @Override
    public void onOrderCreated(Order order) {
        System.out.println("[OBSERVER - EMAIL] Отправлен чек на " + order.getUser().getEmail() +
                " за заказ #" + order.getId() + " на сумму " + order.getTotalPrice() + " руб.");
    }
}

