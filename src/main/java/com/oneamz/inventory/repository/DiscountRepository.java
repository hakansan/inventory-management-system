package com.oneamz.inventory.repository;

import com.oneamz.inventory.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByProductIdAndActiveTrue(Long id);
}
