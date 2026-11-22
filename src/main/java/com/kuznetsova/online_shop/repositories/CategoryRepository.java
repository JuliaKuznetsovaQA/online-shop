package com.kuznetsova.online_shop.repositories;

import com.kuznetsova.online_shop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
