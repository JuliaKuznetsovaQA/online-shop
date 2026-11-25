package com.kuznetsova.online_shop.repositories;

import com.kuznetsova.online_shop.models.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "items", "items.product"}) // защищает историю заказов от проблемы N+1 на два уровня в глубину
    List<Order> findAll();

    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

}
