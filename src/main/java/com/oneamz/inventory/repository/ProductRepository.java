package com.oneamz.inventory.repository;

import com.oneamz.inventory.model.entity.Category;
import com.oneamz.inventory.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndActiveTrue(Category category);
    List<Product> findByActiveTrue();
}
