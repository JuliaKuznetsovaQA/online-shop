package com.kuznetsova.online_shop.observers;

import com.kuznetsova.online_shop.models.Order;

public interface OrderObserver {
    void onOrderCreated(Order order);
}
